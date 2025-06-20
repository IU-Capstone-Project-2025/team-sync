package ru.teamsync.projects.repository;

import java.util.List;

import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.entity.ProjectStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByStatus(ProjectStatus status);
    List<Project> findByTeamLeadId(Long teamLeadId);
    List<Project> findByCourseName(String courseName);

    @Query("SELECT p FROM Project p " +
       "LEFT JOIN p.skills s " +
       "LEFT JOIN p.roles r " +
       "WHERE (:courseName IS NULL OR p.courseName = :courseName) " +
       "AND (:skillIds IS NULL OR s.id IN :skillIds) " +
       "AND (:roleIds IS NULL OR r.id IN :roleIds) " +
       "GROUP BY p.id")
    Page<Project> findByFilters(@Param("skillIds") List<Long> skillIds,
                                @Param("roleIds") List<Long> roleIds,
                                @Param("courseName") String courseName,
                                Pageable pageable);
}
