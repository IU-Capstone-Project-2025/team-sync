package ru.teamsync.projects.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import ru.teamsync.projects.dto.request.ProjectCreateRequest;
import ru.teamsync.projects.dto.request.ProjectUpdateRequest;
import ru.teamsync.projects.dto.response.ProjectResponse;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.entity.ProjectStatus;
import ru.teamsync.projects.mapper.ProjectMapper;
import ru.teamsync.projects.repository.ProjectRepository;
import ru.teamsync.projects.service.exception.ProjectNotFoundException;
import ru.teamsync.projects.service.exception.ResourceAccessDeniedException;
import ru.teamsync.projects.specification.ProjectSpecifications;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public void createProject(ProjectCreateRequest request, Long userId) {
        Project project = projectMapper.toEntity(request, userId);
        projectRepository.save(project);
    }

    public void updateProject(Long projectId, ProjectUpdateRequest request, Long currentUserId) throws NotFoundException, AccessDeniedException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.withId(projectId));

        if (!project.getTeamLeadId().equals(currentUserId)) {
            throw new ResourceAccessDeniedException("You cannot edit this project");
        }

        projectMapper.updateEntity(request, project);
        projectRepository.save(project);
    }

    public Page<ProjectResponse> getProjects(List<Long> skillIds, List<Long> roleIds, Long courseId, ProjectStatus status, Pageable pageable) {
        boolean hasFilters = (skillIds != null && !skillIds.isEmpty()) ||
                (roleIds != null && !roleIds.isEmpty()) ||
                (courseId != null) ||
                (status != null);

        Page<Project> projects;

        if (!hasFilters) {
            projects = projectRepository.findAll(pageable);
        } else {
            Specification<Project> spec = (root, query, cb) -> cb.conjunction();

            if (courseId != null) {
                spec = spec.and(ProjectSpecifications.hasCourseId(courseId));
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

            projects = projectRepository.findAll(spec, pageable);
        }

        return projects.map(projectMapper::toDto);
    }

    public Page<ProjectResponse> getProjectsByTeamLead(Long teamLeadId, Pageable pageable) {
        return projectRepository.findAllByTeamLeadId(teamLeadId, pageable)
                .map(projectMapper::toDto);
    }

    public void deleteProject(Long projectId, Long currentUserId) throws NotFoundException, SecurityException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(NotFoundException::new);

        if (!project.getTeamLeadId().equals(currentUserId)) {
            throw new ResourceAccessDeniedException("You cannot delete this project");
        }

        projectRepository.delete(project);
    }
}