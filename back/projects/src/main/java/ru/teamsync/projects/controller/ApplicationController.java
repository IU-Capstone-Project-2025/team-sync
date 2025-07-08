package ru.teamsync.projects.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.teamsync.projects.dto.request.ApplicationRequest;
import ru.teamsync.projects.dto.response.ApplicationResponse;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.dto.response.PageResponse;
import ru.teamsync.projects.service.ApplicationService;
import ru.teamsync.projects.service.SecurityContextService;

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
    public BaseResponse<PageResponse<ApplicationResponse>> getApplicationsByStudent(Pageable pageable) {
        long userId = securityContextService.getCurrentUserId();
        return BaseResponse.of(applicationService.getApplicationsByMember(userId, pageable));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createApplication(@RequestBody ApplicationRequest request) {
        long userId = securityContextService.getCurrentUserId();
        applicationService.createApplication(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.of(null));
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<BaseResponse<Void>> deleteApplication(@PathVariable Long applicationId) {
        long userId = securityContextService.getCurrentUserId();
        applicationService.deleteApplication(userId, applicationId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }
}