package ru.teamsync.projects.controller;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import ru.teamsync.projects.dto.request.ApplicationRequest;
import ru.teamsync.projects.dto.response.ApplicationResponse;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.service.ApplicationService;
import ru.teamsync.projects.service.JwtService;

@RestController
@RequestMapping("/applications")
@AllArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;
    private final JwtService jwtService;

    @GetMapping("/project/{projectId}")
    //  @PreAuthorize("hasAnyAuthority('PROFESSOR', 'STUDENT')")
    public BaseResponse<Page<ApplicationResponse>> getApplicationsByProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable,
            @RequestHeader("Authorization") String bearerToken) throws NotFoundException, AccessDeniedException {

        String token = bearerToken.substring("Bearer ".length());

        Long internalId = jwtService.extractUserId(token);
        return BaseResponse.of(applicationService.getApplicationsByProject(internalId, projectId, pageable));
    }

    @GetMapping("/my")
    //   @PreAuthorize("hasAnyAuthority('STUDENT')")
    public BaseResponse<Page<ApplicationResponse>> getApplicationsByStudent(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable,
            @RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring("Bearer ".length());

        Long internalId = jwtService.extractUserId(token);
        return BaseResponse.of(applicationService.getApplicationsByMember(internalId, pageable));
    }

    @PostMapping
    //   @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseEntity<BaseResponse<Void>> createApplication(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody ApplicationRequest request,
            @RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring("Bearer ".length());

        Long internalId = jwtService.extractUserId(token);
        applicationService.createApplication(internalId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.of(null)); // 201 - created
    }

    private Long getInternalId(Jwt jwt) {
        return jwt.getClaim("internal_id");
    }
}
