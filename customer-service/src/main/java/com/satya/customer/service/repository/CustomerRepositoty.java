package com.satya.customer.service.repository;

import com.satya.customer.service.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepositoty extends ReactiveCrudRepository<Customer, Integer> {}
