package ru.teamsync.projects.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.teamsync.projects.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByName(String name);
}