package ru.teamsync.projects.dto.request;

import jakarta.validation.constraints.NotNull;

public record FavouriteProjectRequest(@NotNull Long projectId) {

}
