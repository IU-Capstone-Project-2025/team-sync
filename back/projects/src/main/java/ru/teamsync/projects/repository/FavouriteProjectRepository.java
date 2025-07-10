package ru.teamsync.projects.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.teamsync.projects.entity.FavouriteProject;
import ru.teamsync.projects.entity.Project;

public interface FavouriteProjectRepository extends JpaRepository<FavouriteProject, Long> {
    Page<FavouriteProject> findAllByPersonId(Long personId, Pageable pageable);
    boolean existsByProjectIdAndPersonId(Long projectId, Long personId);
    void deleteByProjectIdAndPersonId(Long projectId, Long personId);

    @Modifying
    @Transactional
    @Query("DELETE FROM FavouriteProject fp WHERE fp.project.id = :projectId")
    void deleteByProjectId(Long projectId);
}
