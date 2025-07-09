package ru.teamsync.projects.dto.response;

import java.time.LocalDateTime;

public record FavouriteProjectResponse(
    Long id,
    Long personId,
    ProjectResponse project,
    LocalDateTime createdAt
) {}
