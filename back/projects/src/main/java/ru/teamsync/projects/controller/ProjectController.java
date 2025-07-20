package ru.teamsync.projects.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ru.teamsync.projects.dto.request.ProjectCreateRequest;
import ru.teamsync.projects.dto.request.ProjectUpdateRequest;
import ru.teamsync.projects.dto.request.UpdateApplicationStatusRequest;
import ru.teamsync.projects.dto.response.ApplicationResponse;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.dto.response.PageResponse;
import ru.teamsync.projects.dto.response.ProjectResponse;
import ru.teamsync.projects.entity.ProjectStatus;
import ru.teamsync.projects.service.ApplicationService;
import ru.teamsync.projects.service.ProjectMemberService;
import ru.teamsync.projects.service.projects.FiltrationParameters;
import ru.teamsync.projects.service.projects.ProjectRecommendationsService;
import ru.teamsync.projects.service.projects.ProjectService;
import ru.teamsync.projects.service.security.SecurityContextService;

import java.nio.file.AccessDeniedException;
import java.util.List;


@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final SecurityContextService securityContextService;
    private final ProjectRecommendationsService projectRecommendationsService;
    private final ApplicationService applicationService;
    private final ProjectMemberService projectMemberService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createProject(
            @RequestBody @Valid ProjectCreateRequest request) {
        long userId = securityContextService.getCurrentUserId();
        projectService.createProject(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.of(null));
    }

    @GetMapping("/my")
    public BaseResponse<Page<ProjectResponse>> getMyProjects(Pageable pageable) {
        long userId = securityContextService.getCurrentUserId();
        Page<ProjectResponse> myProjects = projectService.getProjectsByTeamLead(userId, pageable);
        return BaseResponse.of(myProjects);
    }

    @GetMapping("/{projectId}")
    public BaseResponse<ProjectResponse> getProjectById(@PathVariable Long projectId) {
        long userId = securityContextService.getCurrentUserId();
        ProjectResponse project = projectService.getProjectById(userId, projectId);
        return BaseResponse.of(project);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<BaseResponse<Void>> updateProject(
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectUpdateRequest request) throws AccessDeniedException, NotFoundException {
        long userId = securityContextService.getCurrentUserId();
        projectService.updateProject(projectId, request, userId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }

    @PostMapping("/{projectId}/member/{personId}")
    public ResponseEntity<BaseResponse<Void>> removeMemberFromProject(
            @PathVariable Long projectId,
            @PathVariable Long personId) {

        Long userId = securityContextService.getCurrentUserId();
        projectMemberService.removeMembersFromProject(projectId, userId, personId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }


    @GetMapping
    public BaseResponse<Page<ProjectResponse>> getProjects(
            @RequestParam(required = false) List<Long> skillIds,
            @RequestParam(required = false) List<Long> roleIds,
            @RequestParam(required = false) List<Long> courseIds,
            @RequestParam(required = false) ProjectStatus status,
            Pageable pageable
    ) {
        Page<ProjectResponse> projects = projectService.getProjects(
                new FiltrationParameters(skillIds, roleIds, courseIds, status), pageable
        );
        return BaseResponse.of(projects);
    }

    @GetMapping("/recommendations")
    public BaseResponse<PageResponse<ProjectResponse>> getProjectRecommendations(
            @RequestParam(required = false) List<Long> skillIds,
            @RequestParam(required = false) List<Long> roleIds,
            @RequestParam(required = false) List<Long> courseIds,
            @RequestParam(required = false) ProjectStatus status,
            Pageable pageable
    ) {
        long studentId = securityContextService.getCurrentUserProfileId();
        var filtrationParameters = new FiltrationParameters(skillIds, roleIds, courseIds, status);
        var projects = projectRecommendationsService.getProjectRecommendationsForStudent(studentId, filtrationParameters, pageable);
        var page = PageResponse.withContent(projects);
        return BaseResponse.of(page);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<BaseResponse<Void>> deleteProject(
            @PathVariable Long projectId) throws SecurityException, NotFoundException {
        long userId = securityContextService.getCurrentUserId();
        projectService.deleteProject(projectId, userId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }

    /*
     * Project owner can see applications to their project
     */
    @GetMapping("/{projectId}/applications")
    public Page<ApplicationResponse> getApplicationsForProject(
            @PathVariable Long projectId,
            Pageable pageable) {

        Long userId = securityContextService.getCurrentUserId();
        return applicationService.getApplicationsForProject(projectId, userId, pageable);
    }

    @PatchMapping("/{projectId}/applications/{applicationId}")
    public ApplicationResponse updateApplication(
            @PathVariable Long projectId,
            @PathVariable Long applicationId,
            @RequestBody UpdateApplicationStatusRequest request) {

        Long userId = securityContextService.getCurrentUserId();
        return applicationService.updateApplicationStatus(projectId, applicationId, userId, request.status());
    }
}