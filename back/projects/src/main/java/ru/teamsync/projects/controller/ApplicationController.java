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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ru.teamsync.projects.dto.request.ApplicationRequest;
import ru.teamsync.projects.dto.response.ApplicationResponse;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.service.ApplicationService;
import ru.teamsync.projects.service.security.SecurityContextService;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
@Tag(name = "Applications", description = "Manage project applications from users")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final SecurityContextService securityContextService;

    @Operation(summary = "Get applications by project", description = "Returns a paginated list of applications for a specific project. Available to the project owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved applications"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/project/{projectId}")
    public BaseResponse<Page<ApplicationResponse>> getApplicationsByProject(
            @PathVariable Long projectId,
            Pageable pageable) {
        long userId = securityContextService.getCurrentUserId();
        return BaseResponse.of(applicationService.getApplicationsByProject(userId, projectId, pageable));
    }

    @Operation(summary = "Get current user's applications", description = "Returns all applications submitted by the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved applications")
    })
    @GetMapping("/my")
    public BaseResponse<Page<ApplicationResponse>> getApplicationsByStudent(Pageable pageable) {
        long userId = securityContextService.getCurrentUserId();
        return BaseResponse.of(applicationService.getApplicationsByMember(userId, pageable));
    }

    @Operation(summary = "Submit an application", description = "Creates a new application to join a project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Application successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Project not found or closed")
    })
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createApplication(@RequestBody ApplicationRequest request) {
        long userId = securityContextService.getCurrentUserId();
        applicationService.createApplication(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.of(null));
    }

    @Operation(summary = "Delete an application", description = "Deletes the current user's application to the specified project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    @DeleteMapping("project/{projectId}")
    public ResponseEntity<BaseResponse<Void>> deleteApplication(@PathVariable Long projectId) {
        long userId = securityContextService.getCurrentUserId();
        applicationService.deleteApplication(userId, projectId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }
}