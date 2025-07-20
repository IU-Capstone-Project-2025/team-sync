package ru.teamsync.projects.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.teamsync.projects.dto.request.ApplicationRequest;
import ru.teamsync.projects.dto.response.ApplicationResponse;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.service.ApplicationService;
import ru.teamsync.projects.service.security.SecurityContextService;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final SecurityContextService securityContextService;

    @GetMapping("/project/{projectId}")
    public BaseResponse<Page<ApplicationResponse>> getApplicationsByProject(
            @PathVariable Long projectId,
            Pageable pageable) {
        long userId = securityContextService.getCurrentUserId();
        return BaseResponse.of(applicationService.getApplicationsByProject(userId, projectId, pageable));
    }

    @GetMapping("/my")
    public BaseResponse<Page<ApplicationResponse>> getApplicationsByStudent(Pageable pageable) {
        long userId = securityContextService.getCurrentUserId();
        return BaseResponse.of(applicationService.getApplicationsByMember(userId, pageable));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createApplication(@RequestBody ApplicationRequest request) {
        long userId = securityContextService.getCurrentUserId();
        applicationService.createApplication(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.of(null));
    }

    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<BaseResponse<Void>> deleteApplication(@PathVariable Long projectId) {
        long userId = securityContextService.getCurrentUserId();
        applicationService.deleteApplication(userId, projectId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }
}