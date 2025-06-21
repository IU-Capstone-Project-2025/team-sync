package ru.teamsync.auth.controllers.request;

public record RegisterStudentRequest(
        String studyGroup,
        String description,
        String githubAlias,
        String tgAlias
) {
}
