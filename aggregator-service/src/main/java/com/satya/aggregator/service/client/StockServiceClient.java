package com.satya.aggregator.service.client;

import com.satya.aggregator.service.domain.Ticker;
import com.satya.aggregator.service.dto.PriceUpdate;
import com.satya.aggregator.service.dto.StockPriceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;

public class StockServiceClient {
  public static final Logger log = LoggerFactory.getLogger(StockServiceClient.class);

  private final WebClient client;

  private Flux<PriceUpdate> flux;

  public StockServiceClient(WebClient client) {
    this.client = client;
  }

  public Mono<StockPriceResponse> getStockPrice(Ticker ticker) {
    return this.client
        .get()
        .uri("/stock/{ticker}", ticker)
        .retrieve()
        .bodyToMono(StockPriceResponse.class);
  }

    public Flux<PriceUpdate> getStockPriceStream() {
        if (Objects.nonNull(this.flux)) {
            this.flux = getPriceUpdate();
        }
        return this.flux;
    }

  private Flux<PriceUpdate> getPriceUpdate() {
    return this.client
        .get()
        .uri("/stock/price-stream")
        .accept(MediaType.APPLICATION_NDJSON)
        .retrieve()
        .bodyToFlux(PriceUpdate.class)
        .retryWhen(retry())
        .cache(1);
  }

  private Retry retry() {
      return Retry.fixedDelay(100, Duration.ofSeconds(1))
              .doBeforeRetry(retrySignal -> log.error("Stock service price stream call failed. Retrying...{} ", retrySignal.failure().getMessage()));
  }
}
