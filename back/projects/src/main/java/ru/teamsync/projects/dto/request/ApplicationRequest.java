package ru.teamsync.projects.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.NotNull;

public record ApplicationRequest(
    @NotNull 
    @JsonAlias({"projectId", "project_id"})
    Long projectId 
) {}
