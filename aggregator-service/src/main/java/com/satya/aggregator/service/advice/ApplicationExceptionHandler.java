package com.satya.aggregator.service.advice;

import com.satya.aggregator.service.exceptions.CustomerNotFoundException;
import java.net.URI;
import java.util.function.Consumer;

import com.satya.aggregator.service.exceptions.InvalidTradeRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

  @ExceptionHandler(CustomerNotFoundException.class)
  public ProblemDetail handleExcepton(CustomerNotFoundException ex) {
    return buildProblemDetail(
        HttpStatus.NOT_FOUND,
        ex,
        problem -> {
          problem.setType(URI.create("https://example.com/customer-not-found"));
          problem.setTitle("Customer Not Found");
        });
  }

  @ExceptionHandler(InvalidTradeRequestException.class)
  public ProblemDetail handleExcepton(InvalidTradeRequestException ex) {
    return buildProblemDetail(
        HttpStatus.BAD_REQUEST,
        ex,
        problem1 -> {
          problem1.setType(URI.create("https://example.com/invalid-trade-request"));
          problem1.setTitle("Invalid Trade Request");
        });
  }

  private ProblemDetail buildProblemDetail(
      HttpStatus status, Exception ex, Consumer<ProblemDetail> consumer) {
    var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
    consumer.accept(problem);
    return problem;
  }
}
