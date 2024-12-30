package com.satya.customer.service.dto;

import com.satya.customer.service.domain.Ticker;
import com.satya.customer.service.domain.TradeAction;

public record StockTradeRequest(Ticker ticker,
                                Integer price,
                                Integer quantity,
                                TradeAction action) {

    public Integer totalPrice() {
        return this.price() * this.quantity();
    }
}
