package ru.teamsync.projects.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.teamsync.projects.entity.Course;
import ru.teamsync.projects.repository.CourseRepository;

import java.util.List;


@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    public Course createCourseByName(String name) {
        Course course = new Course();
        course.setName(name);
        return courseRepository.save(course);
    }
}
