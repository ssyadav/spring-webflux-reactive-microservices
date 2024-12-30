package com.satya.aggregator.service.dto;


import com.satya.aggregator.service.domain.Ticker;
import com.satya.aggregator.service.domain.TradeAction;

public record TradeRequest(Ticker ticker,
                           TradeAction action,
                           Integer quantity) {}
