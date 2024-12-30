package com.satya.aggregator.service.controller;

import com.satya.aggregator.service.client.StockServiceClient;
import com.satya.aggregator.service.dto.PriceUpdate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("stock")
public class StockPriceStreamController {

  private final StockServiceClient stockServiceClient;

  public StockPriceStreamController(StockServiceClient stockServiceClient) {
    this.stockServiceClient = stockServiceClient;
  }

  @GetMapping(value = "/price-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<PriceUpdate> priceStream() {
    return this.stockServiceClient.getStockPriceStream();
  }
}
