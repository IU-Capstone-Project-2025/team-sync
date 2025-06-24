package ru.teamsync.projects.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import ru.teamsync.projects.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Override
    @NonNull
    Page<Role> findAll(@NonNull Pageable pageable);
}
