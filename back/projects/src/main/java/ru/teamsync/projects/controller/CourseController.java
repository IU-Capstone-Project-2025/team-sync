package ru.teamsync.projects.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.entity.Course;
import ru.teamsync.projects.service.CourseService;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public BaseResponse<Page<Course>> getCourses(Pageable pageable) {
        Page<Course> courses = courseService.getCourses(pageable);
        return BaseResponse.of(courses);
    }
}
