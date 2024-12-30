package com.satya.customer.service.dto;

import com.satya.customer.service.domain.Ticker;
import com.satya.customer.service.domain.TradeAction;

public record StockTradeResponse(Integer customerId,
                                 Ticker ticker,
                                 Integer price,
                                 Integer quantity,
                                 TradeAction action,
                                 Integer totalPrice,
                                 Integer balance) {}
