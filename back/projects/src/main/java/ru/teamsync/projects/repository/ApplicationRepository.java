package ru.teamsync.projects.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.teamsync.projects.entity.Application;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.entity.ApplicationStatus;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findAllByProject(Project project, Pageable pageable);

    Page<Application> findAllByPersonId(Long personId, Pageable pageable);

    boolean existsByProjectIdAndPersonIdAndStatusIn(Long projectId, Long personId, List<ApplicationStatus> statuses);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.project.id = :projectId AND a.status = 'APPROVED'")
    int countApprovedApplicationsByProjectId(Long projectId);
    
    Optional<Application> findByProjectIdAndPersonId(Long projectId, Long personId);
}