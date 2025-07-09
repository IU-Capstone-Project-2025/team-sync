package ru.teamsync.projects.dto.response;

import java.time.LocalDateTime;

import ru.teamsync.projects.entity.ApplicationStatus;

public record ApplicationResponse(
    Long applicationId,
    ProjectResponse project,
    ApplicationStatus status,
    LocalDateTime createdAt
) {}