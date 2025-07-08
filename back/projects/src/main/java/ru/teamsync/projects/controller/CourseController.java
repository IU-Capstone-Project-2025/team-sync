package ru.teamsync.projects.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.entity.Course;
import ru.teamsync.projects.service.CourseService;
import ru.teamsync.projects.service.exception.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public BaseResponse<List<Course>> getCourses() {
        List<Course> courses = courseService.getCourses();
        if (courses.isEmpty()) {
            throw new NotFoundException("No courses found");
        }
        return BaseResponse.of(courses);
    }
}
