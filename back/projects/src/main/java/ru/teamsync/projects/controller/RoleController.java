package ru.teamsync.projects.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.entity.Role;
import ru.teamsync.projects.service.RoleService;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public BaseResponse<Page<Role>> getRoles(Pageable pageable) {
        return BaseResponse.of(roleService.getRoles(pageable));
    }
}