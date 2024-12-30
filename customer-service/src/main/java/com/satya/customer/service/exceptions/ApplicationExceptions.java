package com.satya.customer.service.exceptions;


import reactor.core.publisher.Mono;

public class ApplicationExceptions {

    public static <T> Mono<T> customerNotFound(Integer customerId) {
        return Mono.error(new CustomerNotFoundException(customerId));
    }

    public static <T> Mono<T> insufficientBalance(Integer customerId, Integer quantity, String ticker) {
    return Mono.error(new InsufficientBalanceException(customerId, quantity, ticker));
    }

    public static <T> Mono<T> insufficientShares(Integer customerId, Integer quantity, String ticker) {
    return Mono.error(new InsufficientShareException(customerId, quantity, ticker));
    }

}
