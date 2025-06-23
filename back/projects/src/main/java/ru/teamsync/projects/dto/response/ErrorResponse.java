package ru.teamsync.projects.dto.response;

public record ErrorResponse(
        String code,
        String message
) {
}
