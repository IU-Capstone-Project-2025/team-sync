package ru.teamsync.resume.mapper;

import org.mapstruct.*;
import ru.teamsync.resume.dto.request.UpdateProfessorProfileRequest;
import ru.teamsync.resume.dto.request.UpdateStudentProfileRequest;
import ru.teamsync.resume.dto.response.ProfessorProfileResponse;
import ru.teamsync.resume.dto.response.StudentProfileResponse;
import ru.teamsync.resume.entity.Professor;
import ru.teamsync.resume.entity.Student;
import ru.teamsync.resume.entity.StudyGroup;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "tgAlias", source = "professor.tgAlias")
    ProfessorProfileResponse toResponse(Professor professor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "tgAlias", source = "request.tgAlias")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfessor(UpdateProfessorProfileRequest request, @MappingTarget Professor professor);

    @Mapping(target = "studyGroup", source = "student.studyGroup")
    @Mapping(target = "description", source = "student.description")
    @Mapping(target = "githubAlias", source = "student.githubAlias")
    @Mapping(target = "tgAlias", source = "student.tgAlias")
    @Mapping(target = "skills", source = "student.skills")
    @Mapping(target = "roles", source = "student.roles")
    StudentProfileResponse toResponse(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "githubAlias", source = "request.githubAlias")
    @Mapping(target = "tgAlias", source = "request.tgAlias")
    @Mapping(target = "skills", source = "request.skills")
    @Mapping(target = "roles", source = "request.roles")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStudent(UpdateStudentProfileRequest request, StudyGroup studyGroup, @MappingTarget Student student);

}
