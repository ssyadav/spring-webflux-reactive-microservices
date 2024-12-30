package com.satya.aggregator.service.config;

import com.satya.aggregator.service.client.CustomerServiceClient;
import com.satya.aggregator.service.client.StockServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServiceClientConfig {

  public static final Logger log = LoggerFactory.getLogger(ServiceClientConfig.class);

  @Bean
  public CustomerServiceClient customerServiceClient(
      @Value("${customer.service.url}") String baseUrl) {
    return new CustomerServiceClient(this.create(baseUrl));
  }

  @Bean
  public StockServiceClient stockServiceClient(@Value("${stock.service.url}") String baseUrl) {
    return new StockServiceClient(this.create(baseUrl));
  }

  private WebClient create(String url) {
    log.info("base url: {}", url);
    return WebClient.builder().baseUrl(url).build();
  }
}
