package com.satya.aggregator.service.dto;


import com.satya.aggregator.service.domain.Ticker;
import com.satya.aggregator.service.domain.TradeAction;

public record StockTradeResponse(Integer customerId,
                                 Ticker ticker,
                                 Integer price,
                                 Integer quantity,
                                 TradeAction action,
                                 Integer totalPrice,
                                 Integer balance) {}
