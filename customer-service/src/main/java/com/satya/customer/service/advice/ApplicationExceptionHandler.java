package com.satya.customer.service.advice;

import com.satya.customer.service.exceptions.CustomerNotFoundException;
import com.satya.customer.service.exceptions.InsufficientBalanceException;
import com.satya.customer.service.exceptions.InsufficientShareException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.util.function.Consumer;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleExcepton(CustomerNotFoundException ex) {
        return buildProblemDetail(HttpStatus.NOT_FOUND, ex, problem -> {
            problem.setType(URI.create("https://example.com/customer-not-found"));
            problem.setTitle("Customer Not Found");
        });
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ProblemDetail handleExcepton(InsufficientBalanceException ex) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, ex, problem1 -> {
            problem1.setType(URI.create("https://example.com/insufficient-balance"));
            problem1.setTitle("Insufficient Balance");
        });
    }

    @ExceptionHandler(InsufficientShareException.class)
    public ProblemDetail handleExcepton(InsufficientShareException ex) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, ex, problem1 -> {
            problem1.setType(URI.create("https://example.com/insufficient-shares"));
            problem1.setTitle("Insufficient Shares");
        });
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, Exception ex, Consumer<ProblemDetail> consumer) {
        var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        consumer.accept(problem);
        return problem;

    }
}
