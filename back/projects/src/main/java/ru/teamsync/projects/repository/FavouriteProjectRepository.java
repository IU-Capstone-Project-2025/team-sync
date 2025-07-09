package ru.teamsync.projects.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.teamsync.projects.entity.FavouriteProject;

public interface FavouriteProjectRepository extends JpaRepository<FavouriteProject, Long> {
    Page<FavouriteProject> findAllByPersonId(Long personId, Pageable pageable);
    boolean existsByProjectIdAndPersonId(Long projectId, Long personId);
    void deleteByProjectIdAndPersontId(Long projectId, Long personId);
}
