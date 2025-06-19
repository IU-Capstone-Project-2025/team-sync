package ru.teamsync.projects.repository;

import ru.teamsync.projects.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
