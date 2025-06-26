package ru.teamsync.auth.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import ru.teamsync.auth.client.dto.professor.ProfessorCreationRequest;
import ru.teamsync.auth.client.dto.professor.ProfessorCreationResponse;
import ru.teamsync.auth.client.dto.student.StudentCreationRequest;
import ru.teamsync.auth.client.dto.student.StudentCreationResponse;
import ru.teamsync.auth.controllers.response.BaseResponse;

@HttpExchange(url = "/resume/api/v1")
public interface ResumeClient {

    @PostExchange("/student")
    ResponseEntity<BaseResponse<StudentCreationResponse>> createStudent(StudentCreationRequest request);

    @PostExchange("/professor")
    ResponseEntity<BaseResponse<ProfessorCreationResponse>> createProfessor(ProfessorCreationRequest request);

}
