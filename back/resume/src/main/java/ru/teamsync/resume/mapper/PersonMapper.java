package ru.teamsync.resume.mapper;

import org.mapstruct.Mapper;

import ru.teamsync.resume.dto.response.PersonResponse;
import ru.teamsync.resume.entity.Person;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonResponse toResponse(Person person);
}
