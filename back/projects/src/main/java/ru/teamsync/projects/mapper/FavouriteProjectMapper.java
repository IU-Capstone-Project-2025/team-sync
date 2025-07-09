package ru.teamsync.projects.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.teamsync.projects.dto.response.FavouriteProjectResponse;
import ru.teamsync.projects.entity.FavouriteProject;

@Mapper(componentModel = "spring", uses = {ProjectMapper.class})
public interface FavouriteProjectMapper {

    @Mapping(target = "project", source = "project")
    @Mapping(target = "personId", source = "personId")
    FavouriteProjectResponse toDto(FavouriteProject project);
}
