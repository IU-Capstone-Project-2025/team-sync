package ru.teamsync.projects.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.teamsync.projects.dto.response.ApplicationResponse;
import ru.teamsync.projects.entity.Application;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    ApplicationResponse toDto(Application application);
}
