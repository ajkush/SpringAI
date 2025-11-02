package com.ai.weather.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherAnalysisRequest {

    private String location;
    private String userQuery;
    private String activityType;
    private String temperaturePreference;
    private Boolean includeHealthAlerts;
}

