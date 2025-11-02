package com.ai.weather.controller;

import com.ai.weather.model.WeatherAnalysisRequest;
import com.ai.weather.model.WeatherData;
import com.ai.weather.model.WeatherSummaryResponse;
import com.ai.weather.service.SpringAIWeatherService;
import com.ai.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
@Tag(name = "Weather API", description = "AI-powered weather information endpoints")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;
    private final SpringAIWeatherService springAIWeatherService;

    public WeatherController(WeatherService weatherService,
                           SpringAIWeatherService springAIWeatherService) {
        this.weatherService = weatherService;
        this.springAIWeatherService = springAIWeatherService;
    }

    @GetMapping("/{location}")
    @Operation(summary = "Get AI-enhanced weather summary",
               description = "Retrieves current weather data and provides AI-generated insights, recommendations, and health alerts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved weather summary",
                     content = @Content(schema = @Schema(implementation = WeatherSummaryResponse.class))),
        @ApiResponse(responseCode = "404", description = "Location not found"),
        @ApiResponse(responseCode = "503", description = "Weather API or AI service unavailable")
    })
    public ResponseEntity<WeatherSummaryResponse> getWeatherSummary(
            @Parameter(description = "City name or location", example = "Paris")
            @PathVariable String location) {

        logger.info("GET /api/weather/{} - Fetching AI weather summary", location);
        WeatherSummaryResponse summary = springAIWeatherService.generateWeatherSummary(location);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{location}/raw")
    @Operation(summary = "Get raw weather data",
               description = "Retrieves raw weather data from OpenWeatherMap API without AI processing")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved raw weather data",
                     content = @Content(schema = @Schema(implementation = WeatherData.class))),
        @ApiResponse(responseCode = "404", description = "Location not found"),
        @ApiResponse(responseCode = "503", description = "Weather API unavailable")
    })
    public ResponseEntity<WeatherData> getRawWeather(
            @Parameter(description = "City name or location", example = "London")
            @PathVariable String location) {

        logger.info("GET /api/weather/{}/raw - Fetching raw weather data", location);
        WeatherData weather = weatherService.getWeatherByLocation(location);
        return ResponseEntity.ok(weather);
    }

    @GetMapping("/coordinates")
    @Operation(summary = "Get weather by coordinates",
               description = "Retrieves weather data using latitude and longitude coordinates")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved weather data",
                     content = @Content(schema = @Schema(implementation = WeatherData.class))),
        @ApiResponse(responseCode = "400", description = "Invalid coordinates"),
        @ApiResponse(responseCode = "503", description = "Weather API unavailable")
    })
    public ResponseEntity<WeatherData> getWeatherByCoordinates(
            @Parameter(description = "Latitude", example = "48.8566")
            @RequestParam double lat,
            @Parameter(description = "Longitude", example = "2.3522")
            @RequestParam double lon) {

        logger.info("GET /api/weather/coordinates?lat={}&lon={} - Fetching weather data", lat, lon);
        WeatherData weather = weatherService.getWeatherByCoordinates(lat, lon);
        return ResponseEntity.ok(weather);
    }

    @PostMapping("/analyze")
    @Operation(summary = "Custom weather analysis",
               description = "Analyzes weather conditions based on user preferences and activity plans")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully analyzed weather",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Location not found"),
        @ApiResponse(responseCode = "503", description = "Weather API or AI service unavailable")
    })
    public ResponseEntity<Map<String, String>> analyzeWeather(
            @Valid @RequestBody WeatherAnalysisRequest request) {

        logger.info("POST /api/weather/analyze - Analyzing weather for location: {}", request.getLocation());
        String analysis = springAIWeatherService.analyzeWeatherForActivity(request);

        Map<String, String> response = new HashMap<>();
        response.put("analysis", analysis);
        response.put("location", request.getLocation());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat")
    @Operation(summary = "Conversational weather query",
               description = "Ask weather-related questions in natural language")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully processed query",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Invalid query"),
        @ApiResponse(responseCode = "503", description = "AI service unavailable")
    })
    public ResponseEntity<Map<String, String>> chatWeatherQuery(
            @Parameter(description = "Natural language weather question",
                      example = "What should I wear in Tokyo today?")
            @RequestParam String query) {

        logger.info("GET /api/weather/chat?query={}", query);
        String answer = springAIWeatherService.answerWeatherQuery(query);

        Map<String, String> response = new HashMap<>();
        response.put("query", query);
        response.put("answer", answer);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the weather service is running")
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "AI-Powered Weather Information System");
        health.put("ai_provider", "Spring AI + OpenAI (MCP-ready)");
        health.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(health);
    }

    @GetMapping("/{location}/spring-ai")
    @Operation(summary = "Get AI-enhanced weather using Spring AI",
               description = "Uses Spring AI ChatClient with MCP-style context management for weather analysis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved weather summary using Spring AI",
                     content = @Content(schema = @Schema(implementation = WeatherSummaryResponse.class))),
        @ApiResponse(responseCode = "404", description = "Location not found"),
        @ApiResponse(responseCode = "503", description = "Weather API or AI service unavailable")
    })
    public ResponseEntity<WeatherSummaryResponse> getWeatherSummarySpringAI(
            @Parameter(description = "City name or location", example = "Paris")
            @PathVariable String location) {

        logger.info("GET /api/weather/{}/spring-ai - Fetching weather with Spring AI", location);
        WeatherSummaryResponse summary = springAIWeatherService.generateWeatherSummary(location);
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/analyze/spring-ai")
    @Operation(summary = "Custom weather analysis with Spring AI",
               description = "Analyzes weather using Spring AI ChatClient with MCP-style context")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully analyzed weather with Spring AI",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Location not found"),
        @ApiResponse(responseCode = "503", description = "Weather API or AI service unavailable")
    })
    public ResponseEntity<Map<String, String>> analyzeWeatherSpringAI(
            @Valid @RequestBody WeatherAnalysisRequest request) {

        logger.info("POST /api/weather/analyze/spring-ai - Analyzing with Spring AI for location: {}",
                   request.getLocation());
        String analysis = springAIWeatherService.analyzeWeatherForActivity(request);

        Map<String, String> response = new HashMap<>();
        response.put("analysis", analysis);
        response.put("location", request.getLocation());
        response.put("ai_provider", "Spring AI (MCP-ready)");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/spring-ai")
    @Operation(summary = "Conversational weather query with Spring AI",
               description = "Ask weather questions using Spring AI ChatClient")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully processed query with Spring AI",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Invalid query"),
        @ApiResponse(responseCode = "503", description = "AI service unavailable")
    })
    public ResponseEntity<Map<String, String>> chatWeatherQuerySpringAI(
            @Parameter(description = "Natural language weather question",
                      example = "What should I wear in Tokyo today?")
            @RequestParam String query) {

        logger.info("GET /api/weather/chat/spring-ai?query={}", query);
        String answer = springAIWeatherService.answerWeatherQuery(query);

        Map<String, String> response = new HashMap<>();
        response.put("query", query);
        response.put("answer", answer);
        response.put("ai_provider", "Spring AI (MCP-ready)");

        return ResponseEntity.ok(response);
    }
}

