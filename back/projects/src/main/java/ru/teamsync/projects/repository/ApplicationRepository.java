package ru.teamsync.projects.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.teamsync.projects.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findAllByProjectId(Long projectId, Pageable pageable);
    Page<Application> findAllByStudentId(Long studentId, Pageable pageable);
}
