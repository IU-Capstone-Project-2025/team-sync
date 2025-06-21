package ru.teamsync.auth.controllers;

public record ErrorResponse(
        String code,
        String message
) {
}
