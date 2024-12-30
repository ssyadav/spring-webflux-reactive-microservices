package com.satya.customer.service.service;

import com.satya.customer.service.dto.StockTradeRequest;
import com.satya.customer.service.dto.StockTradeResponse;
import com.satya.customer.service.entity.Customer;
import com.satya.customer.service.entity.PortfolioItem;
import com.satya.customer.service.exceptions.ApplicationExceptions;
import com.satya.customer.service.mapper.EntityDtoMapper;
import com.satya.customer.service.repository.CustomerRepositoty;
import com.satya.customer.service.repository.PortfolioItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class TradeService {
  private final CustomerRepositoty customerRepositoty;
  private final PortfolioItemRepository portfolioItemRepository;

  public TradeService(
      CustomerRepositoty customerRepositoty, PortfolioItemRepository portfolioItemRepository) {
    this.customerRepositoty = customerRepositoty;
    this.portfolioItemRepository = portfolioItemRepository;
  }

  @Transactional
  public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest request) {
    return switch (request.action()) {
      case BUY -> this.buyStock(customerId, request);
      case SELL -> this.sellStock(customerId, request);
    };
  }

  private Mono<StockTradeResponse> buyStock(Integer customerId, StockTradeRequest request) {
    var customerMono =
        this.customerRepositoty
            .findById(customerId)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
            .filter(c -> c.getBalance() >= request.totalPrice())
            .switchIfEmpty(
                ApplicationExceptions.insufficientBalance(
                    customerId, request.quantity(), request.ticker().name()));

    var portfolioItemMono =
        this.portfolioItemRepository
            .findByCustomerIdAndTicker(customerId, request.ticker().name())
            .defaultIfEmpty(EntityDtoMapper.toPortfolioItem(customerId, request.ticker()));

    return customerMono
        .zipWhen(customer -> portfolioItemMono)
        .flatMap(t -> this.executeBuy(t.getT1(), t.getT2(), request));
  }

  private Mono<StockTradeResponse> executeBuy(
      Customer customer, PortfolioItem portfolioItem, StockTradeRequest request) {
    var updatedPortfolioItem = EntityDtoMapper.updatePortfolioItem(portfolioItem, request);
    var updatedCustomer = EntityDtoMapper.updateCustomer(customer, request);
    return saveAndBuildResponse(updatedCustomer, updatedPortfolioItem, request);
  }

  private Mono<StockTradeResponse> sellStock(Integer customerId, StockTradeRequest request) {
    var customerMono =
        this.customerRepositoty
            .findById(customerId)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId));

    var portfolioItemMono =
        this.portfolioItemRepository
            .findByCustomerIdAndTicker(customerId, request.ticker().name())
            .defaultIfEmpty(EntityDtoMapper.toPortfolioItem(customerId, request.ticker()))
            .filter(p -> p.getQuantity() >= request.quantity())
            .switchIfEmpty(
                ApplicationExceptions.insufficientShares(
                    customerId, request.quantity(), request.ticker().name()));

    return customerMono
        .zipWhen(customer -> portfolioItemMono)
        .flatMap(t -> this.executeSell(t.getT1(), t.getT2(), request));
  }

  private Mono<StockTradeResponse> executeSell(
      Customer customer, PortfolioItem portfolioItem, StockTradeRequest request) {
    portfolioItem.setQuantity(portfolioItem.getQuantity() - request.quantity());
    customer.setBalance(customer.getBalance() + request.totalPrice());
    return saveAndBuildResponse(customer, portfolioItem, request);
  }

  private Mono<StockTradeResponse> saveAndBuildResponse(
      Customer customer, PortfolioItem portfolioItem, StockTradeRequest request) {
    var response =
        EntityDtoMapper.toStockTradeResponse(request, customer.getId(), customer.getBalance());
    return Mono.zip(
            this.customerRepositoty.save(customer),
            this.portfolioItemRepository.save(portfolioItem))
        .thenReturn(response);
  }
}
