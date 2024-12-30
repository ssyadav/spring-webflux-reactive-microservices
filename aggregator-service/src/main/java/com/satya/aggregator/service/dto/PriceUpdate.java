package com.satya.aggregator.service.dto;

import com.satya.aggregator.service.domain.Ticker;

import java.time.LocalDateTime;

public record PriceUpdate(
        Ticker ticker,
        Integer price,
        LocalDateTime time) {}
