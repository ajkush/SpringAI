package com.ai.weather.service;

import com.ai.weather.exception.AIServiceException;
import com.ai.weather.model.WeatherAnalysisRequest;
import com.ai.weather.model.WeatherData;
import com.ai.weather.model.WeatherSummaryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Rule-based Weather Intelligence Service - 100% FREE (No OpenAI API required!)
 * Uses intelligent algorithms to provide natural language weather insights
 */
@Service("springAIWeatherService")
public class SpringAIWeatherService {

    private static final Logger logger = LoggerFactory.getLogger(SpringAIWeatherService.class);

    private final WeatherService weatherService;

    @Autowired
    public SpringAIWeatherService(WeatherService weatherService) {
        this.weatherService = weatherService;
        logger.info("Rule-based Weather Intelligence Service initialized (FREE - No OpenAI required)");
    }

    public WeatherSummaryResponse generateWeatherSummary(String location) {
        logger.info("Generating intelligent weather summary for: {}", location);

        try {
            WeatherData weatherData = weatherService.getWeatherByLocation(location);

            String aiSummary = generateNaturalLanguageSummary(weatherData);
            String activities = generateActivityRecommendations(weatherData);
            String clothing = generateClothingSuggestions(weatherData);
            String health = generateHealthAlerts(weatherData);

            return WeatherSummaryResponse.builder()
                    .location(weatherData.getName())
                    .country(weatherData.getSys() != null ? weatherData.getSys().getCountry() : null)
                    .temperature(weatherData.getMain() != null ? weatherData.getMain().getTemp() : null)
                    .feelsLike(weatherData.getMain() != null ? weatherData.getMain().getFeelsLike() : null)
                    .condition(weatherData.getPrimaryWeatherCondition())
                    .description(weatherData.getWeatherDescription())
                    .humidity(weatherData.getMain() != null ? weatherData.getMain().getHumidity() : null)
                    .windSpeed(weatherData.getWind() != null ? weatherData.getWind().getSpeed() : null)
                    .pressure(weatherData.getMain() != null ? weatherData.getMain().getPressure() : null)
                    .visibility(weatherData.getVisibility())
                    .aiSummary(aiSummary)
                    .activityRecommendations(activities)
                    .clothingSuggestions(clothing)
                    .healthAlerts(health)
                    .timestamp(weatherData.getDt())
                    .build();

        } catch (Exception e) {
            logger.error("Error generating weather summary: {}", e.getMessage(), e);
            throw new AIServiceException("Failed to generate weather summary", e);
        }
    }

    public String analyzeWeatherForActivity(WeatherAnalysisRequest request) {
        logger.info("Analyzing weather for activity: {} in {}", request.getActivityType(), request.getLocation());

        try {
            WeatherData weatherData = weatherService.getWeatherByLocation(request.getLocation());
            return generateActivityAnalysis(weatherData, request);

        } catch (Exception e) {
            logger.error("Error analyzing weather for activity: {}", e.getMessage(), e);
            throw new AIServiceException("Failed to analyze weather for activity", e);
        }
    }

    public String answerWeatherQuery(String query) {
        logger.info("Answering weather query: {}", query);

        try {
            return "This is a free weather service. To get weather information for a specific location, " +
                   "please use the /api/weather/{location} endpoint. For activity-specific analysis, " +
                   "use the /api/weather/analyze endpoint.";

        } catch (Exception e) {
            logger.error("Error answering weather query: {}", e.getMessage(), e);
            throw new AIServiceException("Failed to answer weather query", e);
        }
    }

    private String generateNaturalLanguageSummary(WeatherData data) {
        StringBuilder summary = new StringBuilder();

        Double temp = data.getMain() != null ? data.getMain().getTemp() : null;
        Double feelsLike = data.getMain() != null ? data.getMain().getFeelsLike() : null;
        String condition = data.getWeatherDescription();

        summary.append(String.format("Currently in %s, ", data.getName()));

        if (temp != null) {
            summary.append(String.format("the temperature is %.1f°C", temp));

            if (feelsLike != null && Math.abs(temp - feelsLike) > 2) {
                summary.append(String.format(", but it feels like %.1f°C", feelsLike));
            }
            summary.append(". ");
        }

        if (condition != null) {
            summary.append(String.format("The weather is %s. ", condition));
        }

        if (data.getWind() != null && data.getWind().getSpeed() != null) {
            double windSpeed = data.getWind().getSpeed();
            if (windSpeed > 10) {
                summary.append(String.format("It's quite windy with speeds of %.1f m/s. ", windSpeed));
            } else if (windSpeed > 5) {
                summary.append(String.format("There's a light breeze at %.1f m/s. ", windSpeed));
            }
        }

        if (data.getMain() != null && data.getMain().getHumidity() != null) {
            int humidity = data.getMain().getHumidity();
            if (humidity > 80) {
                summary.append("It's quite humid today. ");
            } else if (humidity < 30) {
                summary.append("The air is quite dry. ");
            }
        }

        return summary.toString();
    }

    private String generateActivityRecommendations(WeatherData data) {
        StringBuilder recommendations = new StringBuilder();

        Double temp = data.getMain() != null ? data.getMain().getTemp() : null;
        String condition = data.getPrimaryWeatherCondition();
        Double windSpeed = data.getWind() != null ? data.getWind().getSpeed() : null;

        if (temp != null) {
            if (temp > 25) {
                recommendations.append("Perfect weather for swimming, beach activities, or outdoor sports. ");
                recommendations.append("Stay hydrated and use sun protection. ");
            } else if (temp > 15 && temp <= 25) {
                recommendations.append("Great day for hiking, cycling, or a picnic in the park. ");
                recommendations.append("Ideal conditions for most outdoor activities. ");
            } else if (temp > 5 && temp <= 15) {
                recommendations.append("Good for brisk walks, jogging, or autumn outdoor activities. ");
                recommendations.append("Dress in layers for comfort. ");
            } else if (temp > 0 && temp <= 5) {
                recommendations.append("Suitable for winter sports or indoor activities. ");
                recommendations.append("Outdoor activities require warm clothing. ");
            } else {
                recommendations.append("Very cold - best for indoor activities or winter sports with proper gear. ");
            }
        }

        if (condition != null && condition.toLowerCase().contains("rain")) {
            recommendations.append("Indoor activities recommended, or bring waterproof gear. ");
        } else if (condition != null && condition.toLowerCase().contains("clear")) {
            recommendations.append("Perfect visibility for sightseeing and photography. ");
        }

        if (windSpeed != null && windSpeed > 15) {
            recommendations.append("Strong winds - avoid water sports or activities requiring stability. ");
        }

        return recommendations.toString();
    }

    private String generateClothingSuggestions(WeatherData data) {
        StringBuilder clothing = new StringBuilder();

        Double temp = data.getMain() != null ? data.getMain().getTemp() : null;
        Double feelsLike = data.getMain() != null ? data.getMain().getFeelsLike() : null;
        String condition = data.getPrimaryWeatherCondition();
        Double windSpeed = data.getWind() != null ? data.getWind().getSpeed() : null;

        double effectiveTemp = feelsLike != null ? feelsLike : (temp != null ? temp : 20);

        if (effectiveTemp > 28) {
            clothing.append("Light, breathable clothing recommended. ");
            clothing.append("Wear a hat and sunglasses for sun protection. ");
        } else if (effectiveTemp > 20) {
            clothing.append("Comfortable spring/summer attire. ");
            clothing.append("T-shirt and light pants or shorts. ");
        } else if (effectiveTemp > 15) {
            clothing.append("Long sleeves and light jacket recommended. ");
            clothing.append("Comfortable for layering. ");
        } else if (effectiveTemp > 10) {
            clothing.append("Jacket or sweater needed. ");
            clothing.append("Long pants recommended. ");
        } else if (effectiveTemp > 0) {
            clothing.append("Warm coat, hat, and gloves recommended. ");
            clothing.append("Layer up for warmth. ");
        } else {
            clothing.append("Heavy winter coat, thermal layers, hat, scarf, and gloves essential. ");
        }

        if (condition != null && condition.toLowerCase().contains("rain")) {
            clothing.append("Don't forget an umbrella or waterproof jacket! ");
        }

        if (windSpeed != null && windSpeed > 10) {
            clothing.append("Windbreaker or wind-resistant outer layer recommended. ");
        }

        return clothing.toString();
    }

    private String generateHealthAlerts(WeatherData data) {
        StringBuilder alerts = new StringBuilder();

        Double temp = data.getMain() != null ? data.getMain().getTemp() : null;
        Integer humidity = data.getMain() != null ? data.getMain().getHumidity() : null;

        if (temp != null && temp > 30) {
            alerts.append("HIGH HEAT ALERT: Stay hydrated, avoid prolonged sun exposure. ");
            alerts.append("Risk of heat exhaustion or heat stroke. ");
        } else if (temp != null && temp < 0) {
            alerts.append("COLD WEATHER ALERT: Risk of frostbite and hypothermia. ");
            alerts.append("Limit outdoor exposure time. ");
        }

        if (humidity != null && temp != null) {
            if (humidity > 80 && temp > 25) {
                alerts.append("HIGH HUMIDITY: May feel uncomfortable, take frequent breaks in cool areas. ");
            } else if (humidity < 30) {
                alerts.append("LOW HUMIDITY: May cause dry skin and respiratory discomfort. Stay hydrated. ");
            }
        }

        if (temp != null && temp > 20 && temp < 30) {
            alerts.append("UV CAUTION: Use sunscreen if spending extended time outdoors. ");
        }

        if (alerts.length() == 0) {
            alerts.append("No significant health alerts. Conditions are generally comfortable. ");
        }

        return alerts.toString();
    }

    private String generateActivityAnalysis(WeatherData data, WeatherAnalysisRequest request) {
        StringBuilder analysis = new StringBuilder();

        String activity = request.getActivityType() != null ? request.getActivityType().toLowerCase() : "";
        Double temp = data.getMain() != null ? data.getMain().getTemp() : null;
        String condition = data.getPrimaryWeatherCondition();

        analysis.append(String.format("Weather Analysis for %s in %s:\n\n",
                                     request.getActivityType(), request.getLocation()));

        analysis.append("Current Conditions:\n");
        analysis.append(generateNaturalLanguageSummary(data));
        analysis.append("\n\n");

        boolean suitable = true;

        if (activity.contains("swimming") || activity.contains("beach")) {
            if (temp != null && temp < 20) {
                analysis.append("Temperature is too cold for swimming. ");
                suitable = false;
            } else if (condition != null && condition.toLowerCase().contains("rain")) {
                analysis.append("Rainy conditions - not ideal for beach activities. ");
                suitable = false;
            } else {
                analysis.append("Good conditions for beach activities! ");
            }
        } else if (activity.contains("hiking") || activity.contains("running") || activity.contains("cycling")) {
            if (condition != null && condition.toLowerCase().contains("rain")) {
                analysis.append("Rainy conditions - bring waterproof gear or consider rescheduling. ");
                suitable = false;
            } else if (temp != null && (temp < 5 || temp > 30)) {
                analysis.append("Temperature may be uncomfortable for extended outdoor exercise. ");
            } else {
                analysis.append("Excellent conditions for your activity! ");
            }
        } else if (activity.contains("indoor")) {
            analysis.append("Any time is good for indoor activities! ");
        }

        analysis.append("\n\n");
        analysis.append("Recommendations:\n");
        analysis.append(generateActivityRecommendations(data));
        analysis.append("\n\nClothing:\n");
        analysis.append(generateClothingSuggestions(data));

        if (request.getIncludeHealthAlerts() != null && request.getIncludeHealthAlerts()) {
            analysis.append("\n\nHealth Alerts:\n");
            analysis.append(generateHealthAlerts(data));
        }

        return analysis.toString();
    }
}

