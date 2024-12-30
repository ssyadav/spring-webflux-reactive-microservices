package com.satya.customer.service.dto;


import com.satya.customer.service.domain.Ticker;

public record Holding(Ticker ticker,
                      Integer quantity) {}
