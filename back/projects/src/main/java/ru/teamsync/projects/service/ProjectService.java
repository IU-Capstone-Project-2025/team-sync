package ru.teamsync.projects.service;

import org.springframework.stereotype.Service;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.repository.ProjectRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Page<Project> getProjectsByFilters(List<Long> skillIds, List<Long> roleIds, String courseName, Pageable pageable) {
        return projectRepository.findByFilters(skillIds, roleIds, courseName, pageable);
    }
}
