package ru.teamsync.auth.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "teamsync.security.internal-jwt-filter")
public record InternalJwtFilterProperties(
        List<String> skipPathPrefixes
) {
}
