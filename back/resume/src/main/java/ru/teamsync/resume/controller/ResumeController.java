package ru.teamsync.resume.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.teamsync.resume.dto.request.ProfessorCreationRequest;
import ru.teamsync.resume.dto.request.StudentCreationRequest;
import ru.teamsync.resume.dto.response.BaseResponse;
import ru.teamsync.resume.dto.response.ProfessorCreationResponse;
import ru.teamsync.resume.dto.response.StudentCreationResponse;
import ru.teamsync.resume.service.ProfileService;

@RestController
@RequestMapping("/resume/api/v1")
@RequiredArgsConstructor
public class ResumeController {
    private final ProfileService profileService;

    @PostMapping("/student")
    public ResponseEntity<BaseResponse<StudentCreationResponse>> createStudent(
            @RequestBody StudentCreationRequest request) {
        
        StudentCreationResponse response = profileService.createStudentProfile(request);
        return ResponseEntity.ok(BaseResponse.of(response));
    }


    @PostMapping("/professor")
    public ResponseEntity<BaseResponse<ProfessorCreationResponse>> createStudent(
            @RequestBody ProfessorCreationRequest request) {
        
        ProfessorCreationResponse response = profileService.createProfessorProfile(request);
        return ResponseEntity.ok(BaseResponse.of(response));
    }
}
