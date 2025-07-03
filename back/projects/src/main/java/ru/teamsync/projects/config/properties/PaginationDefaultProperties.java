package ru.teamsync.projects.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "teamsync.pagination.default")
public record PaginationDefaultProperties(
        Integer pageNumber,
        Integer pageSize
) {
}
