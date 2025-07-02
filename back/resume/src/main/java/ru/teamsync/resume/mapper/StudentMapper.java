package ru.teamsync.resume.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ru.teamsync.resume.dto.request.UpdateStudentProfileRequest;
import ru.teamsync.resume.dto.response.StudentProfileResponse;
import ru.teamsync.resume.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(target = "studyGroup", source = "student.studyGroup")
    @Mapping(target = "description", source = "student.description")
    @Mapping(target = "githubAlias", source = "student.githubAlias")
    @Mapping(target = "tgAlias", source = "student.tgAlias")
    @Mapping(target = "skills", source = "student.skills")
    @Mapping(target = "roles", source = "student.roles")
    StudentProfileResponse toResponse(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "studyGroup", source = "request.studyGroup")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "githubAlias", source = "request.githubAlias")
    @Mapping(target = "tgAlias", source = "request.tgAlias")
    @Mapping(target = "skills", source = "request.skills")
    @Mapping(target = "roles", source = "request.roles")
    void updateStudent(UpdateStudentProfileRequest request, @MappingTarget Student student);
}