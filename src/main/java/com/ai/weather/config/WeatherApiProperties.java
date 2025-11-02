package com.ai.weather.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "weather.api")
public class WeatherApiProperties {

    private String key;
    private String baseUrl;
    private Integer cacheTtlMinutes = 10;
}

