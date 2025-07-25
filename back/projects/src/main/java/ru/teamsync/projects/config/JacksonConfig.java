package ru.teamsync.projects.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder
                .json()
                .failOnUnknownProperties(false)
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build();
    }
}