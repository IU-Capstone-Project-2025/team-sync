package ru.teamsync.projects.dto.response;

import ru.teamsync.projects.entity.ApplicationStatus;

import java.time.LocalDateTime;

public record ApplicationResponse(
        Long applicationId,
        Long personId,
        ProjectResponse project,
        ApplicationStatus status,
        LocalDateTime createdAt
) {
}