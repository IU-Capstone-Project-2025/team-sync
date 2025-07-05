package ru.teamsync.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.teamsync.projects.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    Page<Project> findAllByTeamLeadId(Long teamLeadId, Pageable pageable);

    @Modifying
    @Query("UPDATE Project p SET p.requiredMembersCount = p.requiredMembersCount - 1 WHERE p.id = :projectId AND p.requiredMembersCount > 0")
    void decrementRequiredMembersCount(Long projectId);
}
