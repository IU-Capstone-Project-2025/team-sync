package ru.teamsync.resume.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import ru.teamsync.resume.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{
    @Override
    @NonNull
    Page<Role> findAll(@NonNull Pageable pageable);

    Page<Role> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
