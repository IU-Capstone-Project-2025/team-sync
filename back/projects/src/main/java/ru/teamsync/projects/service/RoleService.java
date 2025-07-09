package ru.teamsync.projects.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.teamsync.projects.entity.Role;
import ru.teamsync.projects.repository.RoleRepository;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    
    public Page<Role> getRoles(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }
}
