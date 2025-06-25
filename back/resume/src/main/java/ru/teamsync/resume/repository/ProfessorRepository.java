package ru.teamsync.resume.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.teamsync.resume.entity.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByPersonId(Long personId);
    boolean existsByPersonId(Long personId);
}