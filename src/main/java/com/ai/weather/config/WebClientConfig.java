package com.ai.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final WeatherApiProperties apiProperties;

    public WebClientConfig(WeatherApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(apiProperties.getBaseUrl())
                .build();
    }
}

