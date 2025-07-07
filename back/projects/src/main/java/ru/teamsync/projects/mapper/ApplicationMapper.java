package ru.teamsync.projects.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.teamsync.projects.dto.response.ApplicationResponse;
import ru.teamsync.projects.entity.Application;

@Mapper(componentModel = "spring", uses = ProjectMapper.class)
public interface ApplicationMapper {

    @Mapping(target = "applicationId", source = "id")
    @Mapping(target = "project", source = "project")
    ApplicationResponse toDto(Application application);
}