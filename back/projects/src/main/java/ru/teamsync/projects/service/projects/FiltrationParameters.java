package ru.teamsync.projects.service.projects;

import ru.teamsync.projects.entity.ProjectStatus;

import java.util.List;

public record FiltrationParameters(
        List<Long> skillIds,
        List<Long> roleIds,
        List<Long> courseIds,
        ProjectStatus status
) {
}
