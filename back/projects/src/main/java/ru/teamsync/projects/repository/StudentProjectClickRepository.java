package ru.teamsync.projects.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.teamsync.projects.entity.StudentProjectClick;
import ru.teamsync.projects.entity.StudentProjectClickId;

public interface StudentProjectClickRepository extends JpaRepository<StudentProjectClick, StudentProjectClickId> {
    @Override
    Optional<StudentProjectClick> findById(StudentProjectClickId id);
}
