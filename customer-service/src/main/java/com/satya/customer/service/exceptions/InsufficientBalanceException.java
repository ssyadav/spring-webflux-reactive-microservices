package com.satya.customer.service.exceptions;


public class InsufficientBalanceException extends RuntimeException {

    public static final String MESSAGE = "Customer [id=%d] has insufficient balance to buy %d quantity of %s";
    public InsufficientBalanceException(Integer customerId, Integer quantity, String ticker) {
        super(MESSAGE.formatted(customerId, quantity, ticker));
    }
}
