package ru.teamsync.auth.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.teamsync.auth.client.dto.professor.ProfessorCreationRequest;
import ru.teamsync.auth.client.dto.professor.ProfessorCreationResponse;
import ru.teamsync.auth.client.dto.student.StudentCreationRequest;
import ru.teamsync.auth.client.dto.student.StudentCreationResponse;
import ru.teamsync.auth.controllers.response.BaseResponse;

@Service
public class StubResumeClient implements ResumeClient {
    @Override
    public ResponseEntity<BaseResponse<StudentCreationResponse>> createStudent(StudentCreationRequest request) {
        return ResponseEntity.ok(
                BaseResponse.of(new StudentCreationResponse(1))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<ProfessorCreationResponse>> createProfessor(ProfessorCreationRequest request) {
        return ResponseEntity.ok(
                BaseResponse.of(new ProfessorCreationResponse(1))
        );
    }
}
