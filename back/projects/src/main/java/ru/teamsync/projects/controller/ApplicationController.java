package ru.teamsync.projects.controller;

import lombok.AllArgsConstructor;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.teamsync.projects.dto.request.ApplicationRequest;
import ru.teamsync.projects.dto.response.ApplicationResponse;
import ru.teamsync.projects.service.ApplicationService;
import ru.teamsync.projects.dto.response.BaseResponse;
import org.springframework.security.access.AccessDeniedException;

@RestController
@RequestMapping("/applications")
@AllArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyAuthority('PROFESSOR', 'STUDENT')")
    public BaseResponse<Page<ApplicationResponse>> getApplicationsByProject(
            @PathVariable Long projectId, 
            @AuthenticationPrincipal Jwt jwt, 
            Pageable pageable) throws NotFoundException, AccessDeniedException {

        Long internalId = jwt.getClaim("internal_id");
        return BaseResponse.ok(applicationService.getApplicationsByProject(internalId, projectId, pageable));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public BaseResponse<Page<ApplicationResponse>> getApplicationsByStudent(
            @AuthenticationPrincipal Jwt jwt, 
            Pageable pageable) {
        Long internalId = jwt.getClaim("internal_id"); 
        return BaseResponse.ok(applicationService.getApplicationsByMember(internalId, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseEntity<BaseResponse<Void>> createApplication(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody ApplicationRequest request) {
        Long internalId = jwt.getClaim("internal_id"); 
        applicationService.createApplication(internalId, request);
        return ResponseEntity.ok(BaseResponse.ok(null));
    }
}
