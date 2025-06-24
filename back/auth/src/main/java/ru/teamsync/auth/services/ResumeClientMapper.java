package ru.teamsync.auth.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.teamsync.auth.client.dto.PersonCreationRequest;
import ru.teamsync.auth.client.dto.professor.ProfessorCreationRequest;
import ru.teamsync.auth.client.dto.student.StudentCreationRequest;
import ru.teamsync.auth.config.MapstructConfig;
import ru.teamsync.auth.controllers.request.RegisterProfessorRequest;
import ru.teamsync.auth.controllers.request.RegisterStudentRequest;

@Mapper(config = MapstructConfig.class)
public interface ResumeClientMapper {

    @Mapping(target = "person", source = "personCreationRequest")
    StudentCreationRequest toStudentCreationRequest(PersonCreationRequest personCreationRequest, RegisterStudentRequest registerStudentRequest);

    @Mapping(target = "person", source = "personCreationRequest")
    ProfessorCreationRequest toProfessorCreationRequest(PersonCreationRequest personCreationRequest, RegisterProfessorRequest registerProfessorRequest);
}
