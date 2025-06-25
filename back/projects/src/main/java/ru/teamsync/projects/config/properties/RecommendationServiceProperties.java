package ru.teamsync.projects.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "teamsync.client.recommendations")
public record RecommendationServiceProperties (
        String host,
        int port
){
}
