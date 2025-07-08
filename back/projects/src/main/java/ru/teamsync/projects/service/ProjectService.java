package ru.teamsync.projects.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import ru.teamsync.projects.dto.request.ProjectCreateRequest;
import ru.teamsync.projects.dto.request.ProjectUpdateRequest;
import ru.teamsync.projects.dto.response.ApplicationResponse;
import ru.teamsync.projects.dto.response.ProjectResponse;
import ru.teamsync.projects.entity.Application;
import ru.teamsync.projects.entity.ApplicationStatus;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.entity.ProjectMember;
import ru.teamsync.projects.entity.ProjectStatus;
import ru.teamsync.projects.mapper.ApplicationMapper;
import ru.teamsync.projects.mapper.ProjectMapper;
import ru.teamsync.projects.repository.ApplicationRepository;
import ru.teamsync.projects.repository.ProjectMemberRepository;
import ru.teamsync.projects.repository.ProjectRepository;
import ru.teamsync.projects.service.exception.ApplicationNotFoundException;
import ru.teamsync.projects.service.exception.ProjectNotFoundException;
import ru.teamsync.projects.service.exception.ResourceAccessDeniedException;
import ru.teamsync.projects.specification.ProjectSpecifications;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ApplicationRepository applicationRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    
    private final ApplicationMapper applicationMapper;
    private final ProjectMapper projectMapper;

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

    public void deleteProject(Long projectId, Long currentUserId) throws SecurityException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.withId(projectId));

        if (!project.getTeamLeadId().equals(currentUserId)) {
            throw new ResourceAccessDeniedException("You cannot delete this project");
        }

        projectRepository.delete(project);
    }

    public Page<ApplicationResponse> getApplicationsForProject(Long projectId, Long userId, Pageable pageable) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.withId(projectId));

        if (!project.getTeamLeadId().equals(userId)) {
            throw new ResourceAccessDeniedException("You cannot view applications for this project");
        }

        Page<Application> applications = applicationRepository.findAllByProjectId(projectId, pageable);

        return applications.map(applicationMapper::toDto);
    }

    public ApplicationResponse updateApplicationStatus(
            Long projectId, 
            Long applicationId, 
            Long userId, 
            ApplicationStatus status) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.withId(projectId));

        if (!project.getTeamLeadId().equals(userId)) {
            throw new ResourceAccessDeniedException("You cannot view applications for this project");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> ApplicationNotFoundException.withId(applicationId));

        application.setStatus(status);
        applicationRepository.save(application);

        if (status == ApplicationStatus.APPROVED) {
            int approvedCount = applicationRepository.countApprovedApplicationsByProjectId(projectId);
            if (approvedCount >= project.getRequiredMembersCount()) {
                throw new IllegalStateException("Cannot approve — project is already full.");
            }
            
            boolean alreadyMember = projectMemberRepository.existsByProjectIdAndMemberId(projectId, application.getStudentId());
            if (!alreadyMember) {
                ProjectMember member = new ProjectMember();
                member.setProjectId(projectId);
                member.setMemberId(application.getStudentId());
                projectMemberRepository.save(member);
            }
        }

        return applicationMapper.toDto(application);
    }
}