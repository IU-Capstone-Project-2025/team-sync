package ru.teamsync.projects.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.teamsync.projects.entity.Application;
import ru.teamsync.projects.entity.ApplicationStatus;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findAllByProjectId(Long projectId, Pageable pageable);
    Page<Application> findAllByPersonId(Long personId, Pageable pageable);

    boolean existsByProjectIdAndPersonIdAndStatusIn(Long projectId, Long personId, List<ApplicationStatus> statuses);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.projectId = :projectId AND a.status = 'APPROVED'")
    int countApprovedApplicationsByProjectId(Long projectId);
}