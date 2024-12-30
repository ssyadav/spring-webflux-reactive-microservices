package com.satya.customer.service.mapper;


import com.satya.customer.service.domain.Ticker;
import com.satya.customer.service.dto.CustomerInformation;
import com.satya.customer.service.dto.Holding;
import com.satya.customer.service.dto.StockTradeRequest;
import com.satya.customer.service.dto.StockTradeResponse;
import com.satya.customer.service.entity.Customer;
import com.satya.customer.service.entity.PortfolioItem;

import java.util.List;

public class EntityDtoMapper {

    public static CustomerInformation toCustomerInformation(Customer customer, List<PortfolioItem> items) {
    var holdings = items.stream().map(c -> new Holding(c.getTicker(), c.getQuantity())).toList();
        return new CustomerInformation(
                customer.getId(),
                customer.getName(),
                customer.getBalance(),
                holdings);
    }

    public static PortfolioItem toPortfolioItem(Integer customerId, Ticker ticker) {
        var portfolioItem = new PortfolioItem();
        portfolioItem.setCustomerId(customerId);
        portfolioItem.setTicker(ticker);
        portfolioItem.setQuantity(0);
        return portfolioItem;
    }

    public static PortfolioItem updatePortfolioItem(PortfolioItem portfolioItem, StockTradeRequest request) {
        portfolioItem.setQuantity(portfolioItem.getQuantity() + request.quantity());
        return portfolioItem;
    }

    public static Customer updateCustomer(Customer customer, StockTradeRequest request) {
        customer.setBalance(customer.getBalance() - request.totalPrice());
        return customer;
    }

    public static StockTradeResponse toStockTradeResponse(StockTradeRequest request, Integer customerId, Integer balance) {
        return new StockTradeResponse(
                customerId,
                request.ticker(),
                request.price(),
                request.quantity(),
                request.action(),
                request.totalPrice(),
                balance);
    }
}
