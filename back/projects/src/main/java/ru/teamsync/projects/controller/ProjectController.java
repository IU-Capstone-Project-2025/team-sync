package ru.teamsync.projects.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

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

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    //@PreAuthorize("hasAnyAuthority('PROFESSOR', 'STUDENT')")
    public ResponseEntity<BaseResponse<Void>> createProject(@RequestBody @Valid ProjectCreateRequest request) {
        projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(null, true, null));
    }

    @GetMapping("/my")
    //@PreAuthorize("hasAnyAuthority('PROFESSOR', 'STUDENT')")
    public BaseResponse<Page<ProjectResponse>> getMyProjects(AuthenticationPrincipal UserDetailsImpl currentUser, Pageable pageable) {
        Page<ProjectResponse> myProjects = projectService.getProjectsByTeamLead(currentUser.internalId().longValue(), pageable);
        return BaseResponse.ok(myProjects);
    }

    @PutMapping("/{projectId}")
    //@PreAuthorize("hasAnyAuthority('PROFESSOR', 'STUDENT')")
    public ResponseEntity<BaseResponse<Void>> updateProject(
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl currentUser) throws NotFoundException, AccessDeniedException {

        projectService.updateProject(projectId, request, currentUser.internalId().longValue());
        return ResponseEntity.ok(new BaseResponse<>(null, true, null));
    }

    @GetMapping
    //@PreAuthorize("hasAnyAuthority('PROFESSOR', 'STUDENT')")
    public BaseResponse<Page<ProjectResponse>> getProjects(
            @RequestParam(required = false) List<Long> skillIds,
            @RequestParam(required = false) List<Long> roleIds,
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false) ProjectStatus status,
            Pageable pageable) {

        Page<ProjectResponse> projects = projectService.getProjects(skillIds, roleIds, courseName, status, pageable);
        return BaseResponse.ok(projects);
    }

    @DeleteMapping("/{projectId}")
    //@PreAuthorize("hasAnyAuthority('PROFESSOR', 'STUDENT')")
    public ResponseEntity<BaseResponse<Void>> deleteProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) throws NotFoundException {

        projectService.deleteProject(projectId, currentUser.internalId().longValue());
        return ResponseEntity.ok(new BaseResponse<>(null, true, null));
    }
}
