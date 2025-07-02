package ru.teamsync.projects.dto.response;

import java.time.LocalDateTime;

import ru.teamsync.projects.entity.ApplicationStatus;

public record ApplicationResponse(
    Long id,
    Long studentId,
    Long projectId,
    ApplicationStatus status,
    LocalDateTime createdAt
) {}