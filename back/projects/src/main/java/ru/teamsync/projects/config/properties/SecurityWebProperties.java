package ru.teamsync.projects.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "teamsync.security.web")
public record SecurityWebProperties(
        String[] corsAllowedOrigins
) {
}
