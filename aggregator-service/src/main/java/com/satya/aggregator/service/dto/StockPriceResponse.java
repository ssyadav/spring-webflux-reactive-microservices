package com.satya.aggregator.service.dto;

import com.satya.aggregator.service.domain.Ticker;

public record StockPriceResponse(
        Ticker ticker,
        Integer price) {}
