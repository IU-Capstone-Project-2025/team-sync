package ru.teamsync.projects.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import ru.teamsync.projects.dto.request.ProjectCreateRequest;
import ru.teamsync.projects.dto.request.ProjectUpdateRequest;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.dto.response.ProjectResponse;
import ru.teamsync.projects.entity.ProjectStatus;
import ru.teamsync.projects.service.ProjectService;
import ru.teamsync.projects.service.SecurityContextService;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final SecurityContextService securityContextService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createProject(
            @RequestBody @Valid ProjectCreateRequest request) {
        long userId = securityContextService.getCurrentUserId();
        projectService.createProject(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.of(null));
    }

    @GetMapping("/my")
    public BaseResponse<Page<ProjectResponse>> getMyProjects(
            Pageable pageable) {
        long userId = securityContextService.getCurrentUserId();
        Page<ProjectResponse> myProjects = projectService.getProjectsByTeamLead(userId, pageable);
        return BaseResponse.of(myProjects);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<BaseResponse<Void>> updateProject(
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectUpdateRequest request) throws AccessDeniedException, NotFoundException {
        long userId = securityContextService.getCurrentUserId();
        projectService.updateProject(projectId, request, userId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }

    @GetMapping
    public BaseResponse<Page<ProjectResponse>> getProjects(
            @RequestParam(required = false) List<Long> skillIds,
            @RequestParam(required = false) List<Long> roleIds,
            @RequestParam(required = false) List<Long> courseIds,
            @RequestParam(required = false) ProjectStatus status,
            Pageable pageable) {
        Page<ProjectResponse> projects = projectService.getProjects(
                skillIds, roleIds, courseIds, status, pageable
        );
        return BaseResponse.of(projects);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<BaseResponse<Void>> deleteProject(
            @PathVariable Long projectId) throws SecurityException, NotFoundException {
        long userId = securityContextService.getCurrentUserId();
        projectService.deleteProject(projectId, userId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }
}