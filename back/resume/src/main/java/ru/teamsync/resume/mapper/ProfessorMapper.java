package ru.teamsync.resume.mapper;

import org.mapstruct.Mapper;

import ru.teamsync.resume.dto.response.ProfessorProfileResponse;
import ru.teamsync.resume.entity.Professor;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {
    ProfessorProfileResponse toResponse(Professor professor);
}
