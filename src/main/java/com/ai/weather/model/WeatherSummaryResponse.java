package com.ai.weather.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherSummaryResponse {

    private String location;
    private String country;
    private Double temperature;
    private Double feelsLike;
    private String condition;
    private String description;
    private Integer humidity;
    private Double windSpeed;
    private Integer pressure;
    private Integer visibility;
    private String aiSummary;
    private String activityRecommendations;
    private String clothingSuggestions;
    private String healthAlerts;
    private Long timestamp;
}

