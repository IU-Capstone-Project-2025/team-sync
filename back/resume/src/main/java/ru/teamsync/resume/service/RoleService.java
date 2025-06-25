package ru.teamsync.resume.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.teamsync.resume.dto.response.RoleResponse;
import ru.teamsync.resume.entity.Role;
import ru.teamsync.resume.repository.RoleRepository;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository RoleRepository;

    public Page<RoleResponse> getRoles(String search, Pageable pageable) {
        Page<Role> roles;
        if (search != null && !search.isBlank()) {
            roles = RoleRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            roles = RoleRepository.findAll(pageable);
        }
        return roles.map(role -> new RoleResponse(role.getId(), role.getName(), role.getDescription()));
    }
}
