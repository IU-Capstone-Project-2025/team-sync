package ru.teamsync.resume.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.teamsync.resume.dto.response.RoleResponse;
import ru.teamsync.resume.service.RoleService;

@RestController
@RequestMapping("/roles")
@AllArgsConstructor
public class RoleController {

    private final RoleService RoleService;

    @GetMapping
    public ResponseEntity<Page<RoleResponse>> getRoles(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        Page<RoleResponse> roles = RoleService.getRoles(search, pageable);
        return ResponseEntity.ok(roles);
    }
}