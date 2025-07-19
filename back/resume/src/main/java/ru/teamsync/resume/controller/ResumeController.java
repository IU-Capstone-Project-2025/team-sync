package ru.teamsync.resume.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import ru.teamsync.resume.dto.request.ProfessorCreationRequest;
import ru.teamsync.resume.dto.request.StudentCreationRequest;
import ru.teamsync.resume.dto.response.BaseResponse;
import ru.teamsync.resume.dto.response.ProfessorCreationResponse;
import ru.teamsync.resume.dto.response.StudentCreationResponse;
import ru.teamsync.resume.service.ProfileService;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ResumeController {
    private final ProfileService profileService;

    @Operation(summary = "Create student profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student profile successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid student creation request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/student")
    public ResponseEntity<BaseResponse<StudentCreationResponse>> createStudent(
            @RequestBody StudentCreationRequest request) {
        
        StudentCreationResponse response = profileService.createStudentProfile(request);
        return ResponseEntity.ok(BaseResponse.of(response));
    }

    @Operation(summary = "Create professor profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Professor profile successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid professor creation request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/professor")
    public ResponseEntity<BaseResponse<ProfessorCreationResponse>> createStudent(
            @RequestBody ProfessorCreationRequest request) {
        
        ProfessorCreationResponse response = profileService.createProfessorProfile(request);
        return ResponseEntity.ok(BaseResponse.of(response));
    }
}
