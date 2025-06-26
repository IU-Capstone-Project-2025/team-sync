package ru.teamsync.resume.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.teamsync.resume.dto.response.PersonResponse;
import ru.teamsync.resume.entity.Person;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "surname", source = "surname")
    @Mapping(target = "email", source = "email")
    PersonResponse toResponse(Person person);
}
