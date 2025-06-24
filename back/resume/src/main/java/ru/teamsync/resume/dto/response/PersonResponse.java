package ru.teamsync.resume.dto.response;

public record PersonResponse(
    Long id,
    String name,
    String surname,
    String email
) {}
