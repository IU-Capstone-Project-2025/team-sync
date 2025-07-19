package ru.teamsync.projects.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.entity.Role;
import ru.teamsync.projects.service.RoleService;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Retrieve roles used in the system and in projects")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    @Operation(summary = "Get all roles", description = "Returns a paginated list of all roles")
    public BaseResponse<Page<Role>> getRoles(Pageable pageable) {
        return BaseResponse.of(roleService.getRoles(pageable));
    }

    @GetMapping("/in-projects")
    @Operation(summary = "Get roles in projects", description = "Returns a paginated list of roles used in projects")
    public BaseResponse<Page<Role>> getRolesInProject(Pageable pageable) {
        return BaseResponse.of(roleService.getRolesInProjects(pageable));
    }
}