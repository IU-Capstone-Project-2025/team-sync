package ru.teamsync.resume.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ru.teamsync.resume.dto.request.UpdateProfessorProfileRequest;
import ru.teamsync.resume.dto.response.ProfessorProfileResponse;
import ru.teamsync.resume.entity.Professor;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {
    @Mapping(target = "tgAlias", source = "tgAlias")
    ProfessorProfileResponse toResponse(Professor professor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "tgAlias", source = "request.tgAlias")
    void updateProfessor(UpdateProfessorProfileRequest request, @MappingTarget Professor professor);
}