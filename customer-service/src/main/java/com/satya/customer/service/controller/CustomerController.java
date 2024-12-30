package com.satya.customer.service.controller;

import com.satya.customer.service.dto.CustomerInformation;
import com.satya.customer.service.dto.StockTradeRequest;
import com.satya.customer.service.dto.StockTradeResponse;
import com.satya.customer.service.service.CustomerService;
import com.satya.customer.service.service.TradeService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final TradeService tradeService;

    public CustomerController(CustomerService customerService, TradeService tradeService) {
        this.customerService = customerService;
        this.tradeService = tradeService;
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(@PathVariable Integer customerId) {
        return this.customerService.getCustomerInformation(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> trade(@PathVariable Integer customerId, @RequestBody Mono<StockTradeRequest> mono) {
        return mono.flatMap(req -> this.tradeService.trade(customerId, req));
    }

}
