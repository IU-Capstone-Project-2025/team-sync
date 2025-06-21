package ru.teamsync.auth.controllers.respons;

public record ErrorResponse(
        String code,
        String message
) {
}
