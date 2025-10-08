package com.carparkspring.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Car Parking Management System API")
                        .version("1.0.0")
                        .description("Production-ready car parking management system with JWT authentication, optimistic locking, and WebSocket support")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your.email@example.com")));
    }
}
