package ru.teamsync.projects.controller;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import ru.teamsync.projects.dto.request.CourseCreateRequest;
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

    @Operation(
            summary = "Get all courses",
            description = "Retrieves a list of all available courses. Throws an error if none are found."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved course list"),
            @ApiResponse(responseCode = "404", description = "No courses found")
    })
    @GetMapping
    public BaseResponse<List<Course>> getCourses() {
        List<Course> courses = courseService.getCourses();
        if (courses.isEmpty()) {
            throw new NotFoundException("No courses found");
        }
        return BaseResponse.of(courses);
    }

    @Operation(
            summary = "Create a new course",
            description = "Creates a new course using the provided course name."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<BaseResponse<Course>> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        Course savedCourse = courseService.createCourseByName(request.name());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.of(savedCourse));
    }
}
