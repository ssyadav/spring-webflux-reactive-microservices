package com.satya.aggregator.service.dto;


import com.satya.aggregator.service.domain.Ticker;

public record Holding(Ticker ticker,
                      Integer quantity) {}
