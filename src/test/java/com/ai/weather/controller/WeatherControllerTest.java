package com.ai.weather.controller;

import com.ai.weather.model.WeatherAnalysisRequest;
import com.ai.weather.model.WeatherData;
import com.ai.weather.model.WeatherSummaryResponse;
import com.ai.weather.service.SpringAIWeatherService;
import com.ai.weather.service.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private SpringAIWeatherService springAIWeatherService;

    @Test
    void testGetWeatherSummary_Success() throws Exception {
        WeatherSummaryResponse mockResponse = WeatherSummaryResponse.builder()
                .location("Paris")
                .country("FR")
                .temperature(20.5)
                .feelsLike(19.8)
                .condition("Clear")
                .description("clear sky")
                .humidity(65)
                .windSpeed(3.5)
                .pressure(1013)
                .aiSummary("Beautiful weather in Paris!")
                .activityRecommendations("Great for outdoor activities")
                .clothingSuggestions("Light jacket recommended")
                .timestamp(1635789600L)
                .build();

        when(springAIWeatherService.generateWeatherSummary("Paris")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/weather/Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Paris"))
                .andExpect(jsonPath("$.country").value("FR"))
                .andExpect(jsonPath("$.temperature").value(20.5))
                .andExpect(jsonPath("$.condition").value("Clear"));
    }

    @Test
    void testGetRawWeather_Success() throws Exception {
        WeatherData mockWeather = new WeatherData();
        mockWeather.setName("London");

        WeatherData.Main main = new WeatherData.Main();
        main.setTemp(15.0);
        mockWeather.setMain(main);

        when(weatherService.getWeatherByLocation("London")).thenReturn(mockWeather);

        mockMvc.perform(get("/api/weather/London/raw"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("London"))
                .andExpect(jsonPath("$.main.temp").value(15.0));
    }

    @Test
    void testGetWeatherByCoordinates_Success() throws Exception {
        WeatherData mockWeather = new WeatherData();
        mockWeather.setName("Tokyo");

        WeatherData.Main main = new WeatherData.Main();
        main.setTemp(22.0);
        mockWeather.setMain(main);

        when(weatherService.getWeatherByCoordinates(35.6762, 139.6503)).thenReturn(mockWeather);

        mockMvc.perform(get("/api/weather/coordinates")
                        .param("lat", "35.6762")
                        .param("lon", "139.6503"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tokyo"));
    }

    @Test
    void testAnalyzeWeather_Success() throws Exception {
        WeatherAnalysisRequest request = WeatherAnalysisRequest.builder()
                .location("Denver")
                .userQuery("Should I go hiking?")
                .activityType("hiking")
                .includeHealthAlerts(true)
                .build();

        String mockAnalysis = "Great conditions for hiking in Denver!";
        when(springAIWeatherService.analyzeWeatherForActivity(any(WeatherAnalysisRequest.class)))
                .thenReturn(mockAnalysis);

        mockMvc.perform(post("/api/weather/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.analysis").value(mockAnalysis))
                .andExpect(jsonPath("$.location").value("Denver"));
    }

    @Test
    void testChatWeatherQuery_Success() throws Exception {
        String mockAnswer = "You should wear a light jacket and comfortable shoes for Tokyo today.";
        when(springAIWeatherService.answerWeatherQuery(anyString())).thenReturn(mockAnswer);

        mockMvc.perform(get("/api/weather/chat")
                        .param("query", "What should I wear in Tokyo today?"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value(mockAnswer));
    }

    @Test
    void testHealthCheck_Success() throws Exception {
        mockMvc.perform(get("/api/weather/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").exists());
    }
}

