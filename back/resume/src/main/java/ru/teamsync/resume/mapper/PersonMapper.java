package ru.teamsync.resume.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.teamsync.resume.dto.request.UpdateProfessorProfileRequest;
import ru.teamsync.resume.dto.response.PersonResponse;
import ru.teamsync.resume.dto.response.ProfessorProfileResponse;
import ru.teamsync.resume.entity.Person;
import ru.teamsync.resume.entity.Professor;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonResponse toResponse(Person person);
}
