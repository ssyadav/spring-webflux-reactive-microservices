package com.satya.aggregator.service.exceptions;


public class InvalidTradeRequestException extends RuntimeException {

    public InvalidTradeRequestException(String message) {
        super(message);
    }
}
