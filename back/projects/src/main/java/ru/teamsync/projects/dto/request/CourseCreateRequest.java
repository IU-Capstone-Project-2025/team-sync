package ru.teamsync.projects.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CourseCreateRequest(
    @NotBlank
    String name
) {}