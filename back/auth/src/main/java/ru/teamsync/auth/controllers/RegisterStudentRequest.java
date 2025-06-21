package ru.teamsync.auth.controllers;

public record RegisterStudentRequest(
        String studyGroup,
        String description,
        String githubAlias,
        String tgAlias
) {
}
