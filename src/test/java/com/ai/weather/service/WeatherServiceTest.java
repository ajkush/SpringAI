package com.ai.weather.service;

import com.ai.weather.config.WeatherApiProperties;
import com.ai.weather.exception.LocationNotFoundException;
import com.ai.weather.exception.WeatherApiException;
import com.ai.weather.model.WeatherData;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceTest {

    private MockWebServer mockWebServer;
    private WeatherService weatherService;
    private WeatherApiProperties apiProperties;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        apiProperties = new WeatherApiProperties();
        apiProperties.setKey("test-api-key");
        apiProperties.setBaseUrl(mockWebServer.url("/").toString().replaceAll("/$", ""));
        apiProperties.setCacheTtlMinutes(10);

        WebClient webClient = WebClient.builder()
                .baseUrl(apiProperties.getBaseUrl())
                .build();

        weatherService = new WeatherService(webClient, apiProperties);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testGetWeatherByLocation_Success() {
        String mockResponse = """
                {
                    "coord": {"lon": 2.3522, "lat": 48.8566},
                    "weather": [{"id": 800, "main": "Clear", "description": "clear sky", "icon": "01d"}],
                    "base": "stations",
                    "main": {
                        "temp": 20.5,
                        "feels_like": 19.8,
                        "temp_min": 18.0,
                        "temp_max": 22.0,
                        "pressure": 1013,
                        "humidity": 65
                    },
                    "visibility": 10000,
                    "wind": {"speed": 3.5, "deg": 180},
                    "clouds": {"all": 10},
                    "dt": 1635789600,
                    "sys": {"type": 2, "id": 2041230, "country": "FR", "sunrise": 1635745200, "sunset": 1635782400},
                    "timezone": 7200,
                    "id": 2988507,
                    "name": "Paris",
                    "cod": 200
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        WeatherData weather = weatherService.getWeatherByLocation("Paris");

        assertNotNull(weather);
        assertEquals("Paris", weather.getName());
        assertEquals("FR", weather.getSys().getCountry());
        assertEquals(20.5, weather.getMain().getTemp());
        assertEquals("Clear", weather.getPrimaryWeatherCondition());
    }

    @Test
    void testGetWeatherByLocation_NotFound() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("{\"cod\":\"404\",\"message\":\"city not found\"}"));

        assertThrows(LocationNotFoundException.class, () ->
                weatherService.getWeatherByLocation("InvalidCity"));
    }

    @Test
    void testGetWeatherByLocation_NullLocation() {
        assertThrows(IllegalArgumentException.class, () ->
                weatherService.getWeatherByLocation(null));
    }

    @Test
    void testGetWeatherByLocation_EmptyLocation() {
        assertThrows(IllegalArgumentException.class, () ->
                weatherService.getWeatherByLocation("   "));
    }

    @Test
    void testFormatWeatherDataForAI() {
        WeatherData weather = new WeatherData();
        weather.setName("Paris");

        WeatherData.Sys sys = new WeatherData.Sys();
        sys.setCountry("FR");
        weather.setSys(sys);

        WeatherData.Main main = new WeatherData.Main();
        main.setTemp(20.5);
        main.setFeelsLike(19.8);
        main.setTempMin(18.0);
        main.setTempMax(22.0);
        main.setHumidity(65);
        main.setPressure(1013);
        weather.setMain(main);

        WeatherData.Weather weatherCondition = new WeatherData.Weather();
        weatherCondition.setMain("Clear");
        weatherCondition.setDescription("clear sky");
        weather.setWeather(java.util.List.of(weatherCondition));

        WeatherData.Wind wind = new WeatherData.Wind();
        wind.setSpeed(3.5);
        wind.setDeg(180);
        weather.setWind(wind);

        weather.setVisibility(10000);

        String formatted = weatherService.formatWeatherDataForAI(weather);

        assertNotNull(formatted);
        assertTrue(formatted.contains("Paris"));
        assertTrue(formatted.contains("20.5"));
        assertTrue(formatted.contains("Clear"));
        assertTrue(formatted.contains("65%"));
    }
}

