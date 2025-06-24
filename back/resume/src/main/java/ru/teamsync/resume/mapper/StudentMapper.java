package ru.teamsync.resume.mapper;

import org.mapstruct.Mapper;
import ru.teamsync.resume.dto.response.StudentProfileResponse;
import ru.teamsync.resume.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentProfileResponse toResponse(Student student);
}