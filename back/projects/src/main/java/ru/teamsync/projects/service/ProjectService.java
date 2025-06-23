package ru.teamsync.projects.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.entity.ProjectStatus;
import ru.teamsync.projects.mapper.ProjectMapper;
import ru.teamsync.projects.repository.ProjectRepository;
import ru.teamsync.projects.specification.ProjectSpecifications;
import ru.teamsync.projects.dto.request.ProjectCreateRequest;
import ru.teamsync.projects.dto.request.ProjectUpdateRequest;
import ru.teamsync.projects.dto.response.ProjectResponse;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public void createProject(ProjectCreateRequest request) {
        Project project = new Project();
        project.setCourseName(request.getCourseName());
        project.setTeamLeadId(request.getTeamLeadId());
        project.setDescription(request.getDescription());
        project.setProjectLink(request.getProjectLink());
        project.setStatus(ProjectStatus.valueOf(request.getStatus().toUpperCase()));

        project.setSkillIds(request.getSkills());
        project.setRoleIds(request.getRoles());

        projectRepository.save(project);
    }

    public void updateProject(Long projectId, ProjectUpdateRequest request, Long currentUserId) throws NotFoundException, AccessDeniedException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException());

        if (!project.getTeamLeadId().equals(currentUserId)) {
            throw new AccessDeniedException("You cannot edit this project");
        }

        if (request.getCourseName() != null)
            project.setCourseName(request.getCourseName());

        if (request.getTeamLeadId() != null)
            project.setTeamLeadId(request.getTeamLeadId());

        if (request.getDescription() != null)
            project.setDescription(request.getDescription());

        if (request.getProjectLink() != null)
            project.setProjectLink(request.getProjectLink());

        if (request.getStatus() != null)
            project.setStatus(ProjectStatus.valueOf(request.getStatus().toUpperCase()));

        if (request.getSkills() != null) {
            project.setSkillIds(request.getSkills());
        }

        if (request.getRoles() != null) {
            project.setRoleIds(request.getRoles());
        }

        projectRepository.save(project);
    }

    public Page<ProjectResponse> getProjects(List<Long> skillIds, List<Long> roleIds, String courseName, ProjectStatus status, Pageable pageable) {
        boolean hasFilters = (skillIds != null && !skillIds.isEmpty()) ||
                             (roleIds != null && !roleIds.isEmpty()) ||
                             (courseName != null && !courseName.isEmpty()) ||
                             (status != null);

        Page<Project> projects;

        if (!hasFilters) {
            projects = projectRepository.findAll(pageable);
        } else {
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
            throw new SecurityException("You cannot delete this project");
        }

        projectRepository.delete(project);
    }
}
