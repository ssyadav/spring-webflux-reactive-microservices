package com.satya.aggregator.service.service;

import com.satya.aggregator.service.client.CustomerServiceClient;
import com.satya.aggregator.service.client.StockServiceClient;
import com.satya.aggregator.service.domain.Ticker;
import com.satya.aggregator.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerPortfolioService {

  public static final Logger log = LoggerFactory.getLogger(CustomerPortfolioService.class);

  private final CustomerServiceClient customerServiceClient;
  private final StockServiceClient stockServiceClient;

  public CustomerPortfolioService(
      CustomerServiceClient customerServiceClient, StockServiceClient stockServiceClient) {
    this.customerServiceClient = customerServiceClient;
    this.stockServiceClient = stockServiceClient;
  }

  public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
    return this.customerServiceClient.getCustomerInformation(customerId);
  }

    public Mono<StockTradeResponse> trade(Integer customerId, TradeRequest request) {
        return this.stockServiceClient.getStockPrice(request.ticker())
                .map(StockPriceResponse::price)
                .map(price -> this.toStockTradeRequest(request, price))
                .flatMap(req -> this.customerServiceClient.trade(customerId, req));
    }

   private StockTradeRequest toStockTradeRequest(TradeRequest request, Integer price) {
        return new StockTradeRequest(
                request.ticker(),
                price,
                request.quantity(),
                request.action()
        );
    }
}
