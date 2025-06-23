package ru.teamsync.projects.mapper;

import org.mapstruct.Mapper;

import ru.teamsync.projects.dto.response.ProjectResponse;
import ru.teamsync.projects.entity.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toDto(Project project);
}