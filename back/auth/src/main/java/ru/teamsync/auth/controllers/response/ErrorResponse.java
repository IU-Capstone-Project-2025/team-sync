package ru.teamsync.auth.controllers.response;

public record ErrorResponse(
        String code,
        String message
) {
}
