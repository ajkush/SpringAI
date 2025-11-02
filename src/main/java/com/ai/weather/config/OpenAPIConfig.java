package com.ai.weather.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI weatherOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI-Powered Weather Information System API")
                        .description("RESTful API for intelligent weather information with AI-powered insights and recommendations")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Weather AI Team")
                                .email("support@weatherai.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development server")
                ));
    }
}

