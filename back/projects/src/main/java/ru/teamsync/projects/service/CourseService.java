package ru.teamsync.projects.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.teamsync.projects.entity.Course;
import ru.teamsync.projects.repository.CourseRepository;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public Page<Course> getCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }
}
