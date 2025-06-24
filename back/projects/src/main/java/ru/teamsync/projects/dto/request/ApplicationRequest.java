package ru.teamsync.projects.dto.request;

import jakarta.validation.constraints.NotNull;

public record ApplicationRequest(@NotNull Long projectId) {

}
