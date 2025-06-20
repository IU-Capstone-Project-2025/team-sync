package ru.teamsync.projects.controller;

import org.springframework.web.bind.annotation.*;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.service.ProjectService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import ru.teamsync.projects.dto.response.BaseResponse;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<Project> getProjects(
            @RequestParam(required = false) List<Long> skillIds,
            @RequestParam(required = false) List<Long> roleIds,
            @RequestParam(required = false) String courseName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        try {
            //
        } catch (Exception e) {
            //
        }
    }
}
