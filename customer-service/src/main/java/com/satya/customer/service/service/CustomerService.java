package com.satya.customer.service.service;

import com.satya.customer.service.dto.CustomerInformation;
import com.satya.customer.service.entity.Customer;
import com.satya.customer.service.exceptions.ApplicationExceptions;
import com.satya.customer.service.mapper.EntityDtoMapper;
import com.satya.customer.service.repository.CustomerRepositoty;
import com.satya.customer.service.repository.PortfolioItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

  private final CustomerRepositoty customerRepositoty;
  private final PortfolioItemRepository portfolioItemRepository;

  public CustomerService(
      CustomerRepositoty customerRepositoty, PortfolioItemRepository portfolioItemRepository) {
    this.customerRepositoty = customerRepositoty;
    this.portfolioItemRepository = portfolioItemRepository;
  }

  public Mono<CustomerInformation> getCustomerInformation(Integer id) {
    return this.customerRepositoty
        .findById(id)
        .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
        .flatMap(this::buildCustomerINformation);
  }

  private Mono<CustomerInformation> buildCustomerINformation(Customer customer) {
    return this.portfolioItemRepository
        .findByCustomerId(customer.getId())
        .collectList()
        .map(c -> EntityDtoMapper.toCustomerInformation(customer, c));
  }
}
