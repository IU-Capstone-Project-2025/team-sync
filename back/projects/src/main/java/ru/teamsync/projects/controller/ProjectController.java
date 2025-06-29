package ru.teamsync.projects.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import ru.teamsync.projects.dto.request.ProjectCreateRequest;
import ru.teamsync.projects.dto.request.ProjectUpdateRequest;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.dto.response.ProjectResponse;
import ru.teamsync.projects.entity.ProjectStatus;
import ru.teamsync.projects.service.JwtService;
import ru.teamsync.projects.service.ProjectService;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createProject(
            @RequestBody @Valid ProjectCreateRequest request,
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader("Authorization") String bearerToken) {

        String token = bearerToken.substring("Bearer ".length());

        Long internalId = jwtService.extractUserId(token);
        projectService.createProject(request, internalId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.of(null));
    }

    @GetMapping("/my")
    public BaseResponse<Page<ProjectResponse>> getMyProjects(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable,
            @RequestHeader("Authorization") String bearerToken) {

        String token = bearerToken.substring("Bearer ".length());

        Long internalId = jwtService.extractUserId(token);
        Page<ProjectResponse> myProjects = projectService.getProjectsByTeamLead(internalId, pageable);
        return BaseResponse.of(myProjects);
    }

    @PutMapping("/{projectId}")
   // @PreAuthorize("hasAnyAuthority('PROFESSOR', 'STUDENT')")
    public ResponseEntity<BaseResponse<Void>> updateProject(
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader("Authorization") String bearerToken) throws AccessDeniedException, NotFoundException {

        String token = bearerToken.substring("Bearer ".length());

        Long internalId = jwtService.extractUserId(token);
        projectService.updateProject(projectId, request, internalId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }

    @GetMapping
  //  @PreAuthorize("hasAnyAuthority('PROFESSOR', 'STUDENT')")
    public BaseResponse<Page<ProjectResponse>> getProjects(
            @RequestParam(required = false) List<Long> skillIds,
            @RequestParam(required = false) List<Long> roleIds,
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false) ProjectStatus status,
            Pageable pageable,
            @RequestHeader("Authorization") String bearerToken) {
        
        Page<ProjectResponse> projects = projectService.getProjects(
            skillIds, roleIds, courseName, status, pageable
        );
        return BaseResponse.of(projects);
    }

    @DeleteMapping("/{projectId}")
  //  @PreAuthorize("hasAnyAuthority('PROFESSOR', 'STUDENT')")
    public ResponseEntity<BaseResponse<Void>> deleteProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader("Authorization") String bearerToken) throws SecurityException, NotFoundException {

        String token = bearerToken.substring("Bearer ".length());

        Long internalId = jwtService.extractUserId(token);
        projectService.deleteProject(projectId, internalId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }
}
