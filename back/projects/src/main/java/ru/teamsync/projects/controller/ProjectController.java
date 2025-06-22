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
import ru.teamsync.projects.entity.Project;
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
    public ResponseEntity<BaseResponse<Void>> createProject(@RequestBody @Valid ProjectCreateRequest request) {
        projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(null, true, null));
    }

    @GetMapping("/my")
    public BaseResponse<Page<Project>> getMyProjects(@RequestHeader("X-User-Id") Long userId, Pageable pageable) {
        Page<Project> myProjects = projectService.getProjectsByTeamLead(userId, pageable);
        return new BaseResponse<>(myProjects, true, null);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<BaseResponse<Void>> updateProject(
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectUpdateRequest request,
            @RequestHeader("X-User-Id") Long userId) throws NotFoundException, AccessDeniedException {
        projectService.updateProject(projectId, request, userId);
        return ResponseEntity.ok(new BaseResponse<>(null, true, null));
    }

    @GetMapping
    public BaseResponse<Page<Project>> getProjects(@RequestParam(required = false) List<Long> skillIds,
                                     @RequestParam(required = false) List<Long> roleIds,
                                     @RequestParam(required = false) String courseName,
                                     @RequestParam(required = false) ProjectStatus status,
                                     Pageable pageable) {

        Page<Project> projects = projectService.getProjects(skillIds, roleIds, courseName, status, pageable);
        return new BaseResponse<>(projects,true, null);
    }

    @DeleteMapping("/{projectId}") 
    public ResponseEntity<BaseResponse<Void>> deleteProject(
            @PathVariable Long projectId,
            @RequestHeader("X-User-Id") Long userId) throws NotFoundException {
        
        projectService.deleteProject(projectId, userId);
        return ResponseEntity.ok(new BaseResponse<>(null, true, null));
    }
}
