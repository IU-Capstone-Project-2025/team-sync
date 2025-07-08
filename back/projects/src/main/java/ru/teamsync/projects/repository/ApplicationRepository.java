package ru.teamsync.projects.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.teamsync.projects.entity.Application;
import ru.teamsync.projects.entity.Project;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findAllByProject(Project project, Pageable pageable);
    Page<Application> findAllByPersonId(Long personId, Pageable pageable);
}