package ru.teamsync.resume.dto.response;

public record ErrorResponse(
        String code,
        String message
) {
}