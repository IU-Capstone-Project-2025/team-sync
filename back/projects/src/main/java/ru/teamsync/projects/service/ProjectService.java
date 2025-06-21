package ru.teamsync.projects.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.entity.ProjectStatus;
import ru.teamsync.projects.repository.ProjectRepository;
import ru.teamsync.projects.specification.ProjectSpecifications;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Page<Project> getProjects(List<Long> skillIds, List<Long> roleIds, String courseName, ProjectStatus status, Pageable pageable) {
        boolean hasFilters = (skillIds != null && !skillIds.isEmpty()) ||
                           (roleIds != null && !roleIds.isEmpty()) ||
                           (courseName != null && !courseName.isEmpty()) ||
                           (status != null);

        if (!hasFilters) {
            return projectRepository.findAll(pageable);
        }
        
        Specification<Project> spec = (root, query, cb) -> cb.conjunction();

        if (courseName != null && !courseName.isEmpty()) {
            spec = spec.and(ProjectSpecifications.hasCourseName(courseName));
        }

        if (skillIds != null && !skillIds.isEmpty()) {
            spec = spec.and(ProjectSpecifications.hasSkillIds(skillIds));
        }

        if (roleIds != null && !roleIds.isEmpty()) {
            spec = spec.and(ProjectSpecifications.hasRoleIds(roleIds));
        }

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        return projectRepository.findAll(spec, pageable);
    }
}
