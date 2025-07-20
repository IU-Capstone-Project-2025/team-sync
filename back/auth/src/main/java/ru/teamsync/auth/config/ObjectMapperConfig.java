package ru.teamsync.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build();
    }

}
