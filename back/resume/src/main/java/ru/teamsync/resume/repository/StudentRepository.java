package ru.teamsync.resume.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.teamsync.resume.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByPersonId(Long personId);
    boolean existsByPersonId(Long personId);
}
