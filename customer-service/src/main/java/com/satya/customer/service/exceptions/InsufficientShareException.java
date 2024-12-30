package com.satya.customer.service.exceptions;


public class InsufficientShareException extends RuntimeException {

    public static final String MESSAGE = "Customer [id=%d] has insufficient share to sell %d quantity of %s";
    public InsufficientShareException(Integer customerId, Integer quantity, String ticker) {
        super(MESSAGE.formatted(customerId, quantity, ticker));
    }
}
