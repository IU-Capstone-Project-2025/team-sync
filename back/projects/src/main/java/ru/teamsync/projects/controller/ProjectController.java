package ru.teamsync.projects.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.teamsync.projects.dto.request.ProjectCreateRequest;
import ru.teamsync.projects.dto.request.ProjectUpdateRequest;
import ru.teamsync.projects.dto.request.UpdateApplicationStatusRequest;
import ru.teamsync.projects.dto.response.ApplicationResponse;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.dto.response.PageResponse;
import ru.teamsync.projects.dto.response.ProjectResponse;
import ru.teamsync.projects.entity.ProjectStatus;
import ru.teamsync.projects.service.ProjectRecommendationsService;
import ru.teamsync.projects.service.ProjectService;
import ru.teamsync.projects.service.SecurityContextService;


@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Manage projects, their members, and related applications")
public class ProjectController {

    private final ProjectService projectService;
    private final SecurityContextService securityContextService;
    private final ProjectRecommendationsService projectRecommendationsService;

    @Operation(summary = "Create a new project")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Project successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid project data")
    })
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createProject(
            @RequestBody @Valid ProjectCreateRequest request) {
        long userId = securityContextService.getCurrentUserId();
        projectService.createProject(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.of(null));
    }

    @Operation(summary = "Get current user's projects")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved projects")
    @GetMapping("/my")
    public BaseResponse<Page<ProjectResponse>> getMyProjects(Pageable pageable) {
        long userId = securityContextService.getCurrentUserId();
        Page<ProjectResponse> myProjects = projectService.getProjectsByTeamLead(userId, pageable);
        return BaseResponse.of(myProjects);
    }

    @Operation(summary = "Get project by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved project"),
        @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/{projectId}")
    public BaseResponse<ProjectResponse> getProjectById(@PathVariable Long projectId) {
        long userId = securityContextService.getCurrentUserId();
        ProjectResponse project = projectService.getProjectById(userId, projectId);
        return BaseResponse.of(project);
    }

    @Operation(summary = "Get recommended projects for current user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved recommendations")
    @GetMapping("/recommendations")
    public BaseResponse<PageResponse<ProjectResponse>> getProjectRecommendations(Pageable pageable) {
        long userId = securityContextService.getCurrentUserId();
        var projects = projectRecommendationsService.getProjectRecommendationsForUser(userId, pageable);
        var page = PageResponse.withContent(projects);
        return BaseResponse.of(page);
    }

    @Operation(summary = "Update a project by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Project successfully updated"),
        @ApiResponse(responseCode = "403", description = "Access denied - user is not the team lead"),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "409", description = "Project is already completed")
    })
    @PutMapping("/{projectId}")
    public ResponseEntity<BaseResponse<Void>> updateProject(
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectUpdateRequest request) throws AccessDeniedException, NotFoundException {
        long userId = securityContextService.getCurrentUserId();
        projectService.updateProject(projectId, request, userId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }

    @Operation(summary = "Remove a member from a project")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Member removed successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - user is not the team lead"),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "409", description = "Member does not exist in the project")
    })
    @PostMapping("/{projectId}/member/{personId}")
    public ResponseEntity<BaseResponse<Void>> removeMemberFromProject(
            @PathVariable Long projectId,
            @PathVariable Long personId) {

        Long userId = securityContextService.getCurrentUserId();
        projectService.removeMembersFromProject(projectId, userId, personId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }

    @Operation(summary = "Get all projects with optional filters")
    @ApiResponse(responseCode = "200", description = "Projects successfully retrieved")
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

    @Operation(summary = "Delete a project by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Project successfully deleted"),
        @ApiResponse(responseCode = "403", description = "Access denied - not the owner"),
        @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/{projectId}/applications")
    public Page<ApplicationResponse> getApplicationsForProject(
            @PathVariable Long projectId,
            Pageable pageable) {

        Long userId = securityContextService.getCurrentUserId();
        return projectService.getApplicationsForProject(projectId, userId, pageable);
    }

    @Operation(summary = "Get applications for a specific project (team lead only)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Applications retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PatchMapping("/{projectId}/applications/{applicationId}")
    public ApplicationResponse updateApplication(
            @PathVariable Long projectId,
            @PathVariable Long applicationId,
            @RequestBody UpdateApplicationStatusRequest request) {

        Long userId = securityContextService.getCurrentUserId();
        return projectService.updateApplicationStatus(projectId, applicationId, userId, request.status());
    }
}