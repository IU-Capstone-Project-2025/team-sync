package ru.teamsync.projects.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public record ApplicationRequest(
    @NotNull 
    @JsonProperty("projectId")
    Long projectId 
) {}
