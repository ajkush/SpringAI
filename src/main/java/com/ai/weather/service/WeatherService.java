package com.ai.weather.service;

import com.ai.weather.config.WeatherApiProperties;
import com.ai.weather.exception.LocationNotFoundException;
import com.ai.weather.exception.WeatherApiException;
import com.ai.weather.model.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private static final int TIMEOUT_SECONDS = 10;

    private final WebClient webClient;
    private final WeatherApiProperties apiProperties;

    public WeatherService(WebClient webClient, WeatherApiProperties apiProperties) {
        this.webClient = webClient;
        this.apiProperties = apiProperties;
    }

    @Cacheable(value = "weatherData", key = "#location.toLowerCase()")
    public WeatherData getWeatherByLocation(String location) {
        logger.info("Fetching weather data for location: {}", location);

        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty");
        }

        try {
            WeatherData weatherData = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/weather")
                            .queryParam("q", location.trim())
                            .queryParam("appid", apiProperties.getKey())
                            .queryParam("units", "metric")
                            .build())
                    .retrieve()
                    .bodyToMono(WeatherData.class)
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .onErrorResume(WebClientResponseException.NotFound.class, e -> {
                        logger.error("Location not found: {}", location);
                        return Mono.error(new LocationNotFoundException(
                                "Location '" + location + "' not found. Please check the spelling and try again."));
                    })
                    .onErrorResume(WebClientResponseException.Unauthorized.class, e -> {
                        logger.error("Invalid API key");
                        return Mono.error(new WeatherApiException(
                                "Weather API authentication failed. Please check API key configuration."));
                    })
                    .onErrorResume(WebClientResponseException.TooManyRequests.class, e -> {
                        logger.error("API rate limit exceeded");
                        return Mono.error(new WeatherApiException(
                                "Weather API rate limit exceeded. Please try again later."));
                    })
                    .onErrorResume(Exception.class, e -> {
                        logger.error("Error fetching weather data for {}: {}", location, e.getMessage(), e);
                        return Mono.error(new WeatherApiException(
                                "Failed to fetch weather data: " + e.getMessage(), e));
                    })
                    .block();

            if (weatherData == null) {
                throw new WeatherApiException("Received null response from weather API");
            }

            logger.info("Successfully retrieved weather data for: {}", location);
            return weatherData;

        } catch (LocationNotFoundException | WeatherApiException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error fetching weather data: {}", e.getMessage(), e);
            throw new WeatherApiException("Unexpected error occurred while fetching weather data", e);
        }
    }

    @Cacheable(value = "weatherData", key = "'coord_' + #lat + '_' + #lon")
    public WeatherData getWeatherByCoordinates(double lat, double lon) {
        logger.info("Fetching weather data for coordinates: lat={}, lon={}", lat, lon);

        try {
            WeatherData weatherData = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/weather")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("appid", apiProperties.getKey())
                            .queryParam("units", "metric")
                            .build())
                    .retrieve()
                    .bodyToMono(WeatherData.class)
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .onErrorResume(WebClientResponseException.class, e -> {
                        logger.error("Error fetching weather data for coordinates: {}", e.getMessage());
                        return Mono.error(new WeatherApiException(
                                "Failed to fetch weather data for coordinates: " + e.getMessage()));
                    })
                    .block();

            if (weatherData == null) {
                throw new WeatherApiException("Received null response from weather API");
            }

            logger.info("Successfully retrieved weather data for coordinates");
            return weatherData;

        } catch (WeatherApiException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error fetching weather data: {}", e.getMessage(), e);
            throw new WeatherApiException("Unexpected error occurred while fetching weather data", e);
        }
    }

    public String formatWeatherDataForAI(WeatherData weather) {
        StringBuilder sb = new StringBuilder();

        sb.append("Location: ").append(weather.getName());
        if (weather.getSys() != null && weather.getSys().getCountry() != null) {
            sb.append(", ").append(weather.getSys().getCountry());
        }
        sb.append("\n");

        if (weather.getMain() != null) {
            sb.append("Temperature: ").append(weather.getMain().getTemp()).append("°C\n");
            sb.append("Feels Like: ").append(weather.getMain().getFeelsLike()).append("°C\n");
            sb.append("Min/Max: ").append(weather.getMain().getTempMin())
              .append("°C / ").append(weather.getMain().getTempMax()).append("°C\n");
            sb.append("Humidity: ").append(weather.getMain().getHumidity()).append("%\n");
            sb.append("Pressure: ").append(weather.getMain().getPressure()).append(" hPa\n");
        }

        sb.append("Condition: ").append(weather.getPrimaryWeatherCondition()).append("\n");
        sb.append("Description: ").append(weather.getWeatherDescription()).append("\n");

        if (weather.getWind() != null) {
            sb.append("Wind Speed: ").append(weather.getWind().getSpeed()).append(" m/s\n");
            if (weather.getWind().getDeg() != null) {
                sb.append("Wind Direction: ").append(weather.getWind().getDeg()).append("°\n");
            }
        }

        if (weather.getVisibility() != null) {
            sb.append("Visibility: ").append(weather.getVisibility() / 1000.0).append(" km\n");
        }

        if (weather.getClouds() != null) {
            sb.append("Cloudiness: ").append(weather.getClouds().getAll()).append("%\n");
        }

        return sb.toString();
    }
}

