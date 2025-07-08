package ru.teamsync.projects.dto.response;

import java.util.List;

import ru.teamsync.projects.entity.ProjectStatus;

public record ProjectResponse(
    Long id,
    String name,
    String courseName,
    Long teamLeadId,
    String description,
    String projectLink,
    ProjectStatus status,
    List<Long> skillIds,
    List<Long> roleIds,
    Integer requiredMembersCount
) {}
