package ru.teamsync.projects.controller;

import org.springframework.web.bind.annotation.*;

import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.entity.ProjectStatus;
import ru.teamsync.projects.service.ProjectService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public BaseResponse<Page<Project>> getProjects(@RequestParam(required = false) List<Long> skillIds,
                                     @RequestParam(required = false) List<Long> roleIds,
                                     @RequestParam(required = false) String courseName,
                                     @RequestParam(required = false) ProjectStatus status,
                                     Pageable pageable) {

        Page<Project> projects = projectService.getProjects(skillIds, roleIds, courseName, status, pageable);
        return new BaseResponse<>(projects,true, null);
    }
}
