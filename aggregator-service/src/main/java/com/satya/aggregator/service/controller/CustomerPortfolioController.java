package com.satya.aggregator.service.controller;

import com.satya.aggregator.service.dto.CustomerInformation;
import com.satya.aggregator.service.dto.StockTradeResponse;
import com.satya.aggregator.service.dto.TradeRequest;
import com.satya.aggregator.service.service.CustomerPortfolioService;
import com.satya.aggregator.service.validator.RequestValidator;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customer")
public class CustomerPortfolioController {

  private final CustomerPortfolioService customerPortfolioService;

  public CustomerPortfolioController(CustomerPortfolioService customerPortfolioService) {
    this.customerPortfolioService = customerPortfolioService;
  }

  @GetMapping("/{customerId}")
  public Mono<CustomerInformation> getCustomerInformation(@PathVariable Integer customerId) {
    return this.customerPortfolioService.getCustomerInformation(customerId);
  }

  @PostMapping("/{customerId}/trade")
  public Mono<StockTradeResponse> trade(
      @PathVariable Integer customerId, @RequestBody Mono<TradeRequest> mono) {
    return mono.transform(RequestValidator.validate())
        .flatMap(request -> this.customerPortfolioService.trade(customerId, request));
  }
}