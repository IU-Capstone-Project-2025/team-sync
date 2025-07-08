package ru.teamsync.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.teamsync.projects.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}