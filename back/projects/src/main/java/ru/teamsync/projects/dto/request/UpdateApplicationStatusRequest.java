package ru.teamsync.projects.dto.request;

import jakarta.validation.constraints.NotNull;
import ru.teamsync.projects.entity.ApplicationStatus;

public record UpdateApplicationStatusRequest(
    @NotNull
    ApplicationStatus status
) {

}