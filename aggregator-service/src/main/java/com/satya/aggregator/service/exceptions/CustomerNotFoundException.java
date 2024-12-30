package com.satya.aggregator.service.exceptions;


public class CustomerNotFoundException extends RuntimeException{

    public static final String MESSAGE = "Customer [id=%d] is not found";
    public CustomerNotFoundException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}
