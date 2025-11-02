package com.ai.weather.mcp;

import com.ai.weather.model.WeatherData;
import com.ai.weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * MCP-Style Weather Context Provider
 * Provides structured weather data access for AI model consumption
 */
@Configuration
@Component
public class WeatherMcpServerConfig {

    private static final Logger logger = LoggerFactory.getLogger(WeatherMcpServerConfig.class);

    private final WeatherService weatherService;

    public WeatherMcpServerConfig(WeatherService weatherService) {
        this.weatherService = weatherService;
        logger.info("Initialized Weather MCP-style Context Provider");
    }

    /**
     * Get current weather with MCP-style structured output
     */
    public Map<String, Object> getCurrentWeather(String location) {
        logger.info("MCP context request: get_current_weather for location: {}", location);

        try {
            WeatherData weather = weatherService.getWeatherByLocation(location);
            return buildWeatherContext(weather);
        } catch (Exception e) {
            logger.error("Error in MCP get_current_weather: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * Get weather by coordinates with MCP-style structured output
     */
    public Map<String, Object> getWeatherByCoordinates(double latitude, double longitude) {
        logger.info("MCP context request: get_weather_by_coordinates for lat: {}, lon: {}", latitude, longitude);

        try {
            WeatherData weather = weatherService.getWeatherByCoordinates(latitude, longitude);
            return buildWeatherContext(weather);
        } catch (Exception e) {
            logger.error("Error in MCP get_weather_by_coordinates: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * Format weather data for AI consumption (MCP-style)
     */
    public String formatWeatherDataForAI(String location) {
        logger.info("MCP context request: format_weather_data for location: {}", location);

        try {
            WeatherData weather = weatherService.getWeatherByLocation(location);
            return weatherService.formatWeatherDataForAI(weather);
        } catch (Exception e) {
            logger.error("Error in MCP format_weather_data: {}", e.getMessage(), e);
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Build MCP-style context from weather data
     */
    private Map<String, Object> buildWeatherContext(WeatherData weather) {
        Map<String, Object> context = new HashMap<>();

        context.put("location", weather.getName());

        if (weather.getSys() != null && weather.getSys().getCountry() != null) {
            context.put("country", weather.getSys().getCountry());
        }

        if (weather.getMain() != null) {
            Map<String, Object> temperature = new HashMap<>();
            temperature.put("current", weather.getMain().getTemp());
            temperature.put("feels_like", weather.getMain().getFeelsLike());
            temperature.put("min", weather.getMain().getTempMin());
            temperature.put("max", weather.getMain().getTempMax());
            context.put("temperature", temperature);

            context.put("humidity", weather.getMain().getHumidity());
            context.put("pressure", weather.getMain().getPressure());
        }

        context.put("condition", weather.getPrimaryWeatherCondition());
        context.put("description", weather.getWeatherDescription());

        if (weather.getWind() != null) {
            context.put("wind_speed", weather.getWind().getSpeed());
        }

        if (weather.getVisibility() != null) {
            context.put("visibility", weather.getVisibility());
        }

        if (weather.getClouds() != null) {
            context.put("cloudiness", weather.getClouds().getAll());
        }

        context.put("timestamp", weather.getDt());

        return context;
    }
}

