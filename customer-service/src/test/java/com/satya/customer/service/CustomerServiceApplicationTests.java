package com.satya.customer.service;

import com.satya.customer.service.domain.Ticker;
import com.satya.customer.service.domain.TradeAction;
import com.satya.customer.service.dto.StockTradeRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@SpringBootTest
@AutoConfigureWebTestClient
class CustomerServiceApplicationTests {

  public static final Logger log = LoggerFactory.getLogger(CustomerServiceApplicationTests.class);

  @Autowired private WebTestClient client;

  @Test
  public void customerInformation() {
    getCustomer(1, HttpStatus.OK)
        .jsonPath("$.name")
        .isEqualTo("Sam")
        .jsonPath("$.balance")
        .isEqualTo(10000)
        .jsonPath("$.holding")
        .isEmpty();
  }

  @Test
  public void buyAndSell() {
    var buyRequest1 = new StockTradeRequest(Ticker.GOOGLE, 100, 5, TradeAction.BUY);
    trade(2, buyRequest1, HttpStatus.OK)
        .jsonPath("$.balance")
        .isEqualTo(9500)
        .jsonPath("$.totalPrice")
        .isEqualTo(500);

    var buyRequest2 = new StockTradeRequest(Ticker.GOOGLE, 100, 10, TradeAction.BUY);
    trade(2, buyRequest2, HttpStatus.OK)
        .jsonPath("$.balance")
        .isEqualTo(8500)
        .jsonPath("$.totalPrice")
        .isEqualTo(1000);

    // check the holdings - should get the total quantity of the stock as 15(5+10)
    getCustomer(2, HttpStatus.OK)
        .jsonPath("$.holding")
        .isNotEmpty()
        .jsonPath("$.holding.length()")
        .isEqualTo(1)
        .jsonPath("$.holding[0].ticker")
        .isEqualTo("GOOGLE")
        .jsonPath("$.holding[0].quantity")
        .isEqualTo(15);

    var sellRequest1 = new StockTradeRequest(Ticker.GOOGLE, 120, 5, TradeAction.SELL);
    trade(2, sellRequest1, HttpStatus.OK)
        .jsonPath("$.balance")
        .isEqualTo(9100)
        .jsonPath("$.totalPrice")
        .isEqualTo(600);

    var sellRequest2 = new StockTradeRequest(Ticker.GOOGLE, 130, 10, TradeAction.SELL);
    trade(2, sellRequest2, HttpStatus.OK)
        .jsonPath("$.balance")
        .isEqualTo(10400)
        .jsonPath("$.totalPrice")
        .isEqualTo(1300);

    // check the holdings - should get the total quantity of the stock as 15(5+10)
    getCustomer(2, HttpStatus.OK)
        .jsonPath("$.holding")
        .isNotEmpty()
        .jsonPath("$.holding.length()")
        .isEqualTo(1)
        .jsonPath("$.holding[0].ticker")
        .isEqualTo("GOOGLE")
        .jsonPath("$.holding[0].quantity")
        .isEqualTo(0);
  }

  @Test
  public void customerNotFound() {
    getCustomer(100, HttpStatus.NOT_FOUND)
        .jsonPath("$.detail")
        .isEqualTo("Customer [id=100] is not found");

    var sellRequest = new StockTradeRequest(Ticker.GOOGLE, 130, 10, TradeAction.SELL);
    trade(100, sellRequest, HttpStatus.NOT_FOUND)
        .jsonPath("$.detail")
        .isEqualTo("Customer [id=100] is not found");
  }

  @Test
  public void insufficientBalance() {
    var buyRequest = new StockTradeRequest(Ticker.GOOGLE, 100, 500, TradeAction.BUY);
    trade(2, buyRequest, HttpStatus.BAD_REQUEST)
        .jsonPath("$.detail")
        .isEqualTo("Customer [id=2] has insufficient balance to buy 500 quantity of GOOGLE");
  }

  @Test
  public void insufficientStock() {
    var sellRequest = new StockTradeRequest(Ticker.GOOGLE, 130, 20, TradeAction.SELL);
    trade(2, sellRequest, HttpStatus.BAD_REQUEST)
        .jsonPath("$.detail")
        .isEqualTo("Customer [id=2] has insufficient share to sell 20 quantity of GOOGLE");
  }

  private WebTestClient.BodyContentSpec getCustomer(Integer customerId, HttpStatus status) {
    return this.client
        .get()
        .uri("/customers/{customerId}", customerId)
        .exchange()
        .expectStatus()
        .isEqualTo(status)
        .expectBody()
        .consumeWith(
            response ->
                log.info(
                    "Response: {}",
                    new String(Objects.requireNonNull(response.getResponseBody()))));
  }

  private WebTestClient.BodyContentSpec trade(
      Integer customerId, StockTradeRequest request, HttpStatus status) {
    return this.client
        .post()
        .uri("/customers/{customerId}/trade", customerId)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isEqualTo(status)
        .expectBody()
        .consumeWith(
            response ->
                log.info(
                    "Response: {}",
                    new String(Objects.requireNonNull(response.getResponseBody()))));
  }
}
