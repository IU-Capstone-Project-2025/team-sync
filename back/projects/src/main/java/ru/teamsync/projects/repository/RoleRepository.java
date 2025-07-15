package ru.teamsync.projects.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import ru.teamsync.projects.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Override
    @NonNull
    Page<Role> findAll(@NonNull Pageable pageable);

    @Query(value = "SELECT DISTINCT r.* FROM role r JOIN project_role pr ON r.id = pr.role_id",
            countQuery = "SELECT COUNT(DISTINCT pr.role_id) FROM project_role pr",
            nativeQuery = true)
    Page<Role> findRolesInProjects(Pageable pageable);
}
