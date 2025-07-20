package ru.teamsync.projects.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.teamsync.projects.dto.request.ApplicationRequest;
import ru.teamsync.projects.dto.response.ApplicationResponse;
import ru.teamsync.projects.entity.Application;
import ru.teamsync.projects.entity.ApplicationStatus;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.entity.ProjectStatus;
import ru.teamsync.projects.mapper.ApplicationMapper;
import ru.teamsync.projects.repository.ApplicationRepository;
import ru.teamsync.projects.repository.ProjectMemberRepository;
import ru.teamsync.projects.repository.ProjectRepository;
import ru.teamsync.projects.service.exception.ProjectNotFoundException;
import ru.teamsync.projects.service.exception.ResourceAccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ApplicationService {
    private final ProjectRepository projectRepository;
    private final ApplicationRepository applicationRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ApplicationMapper applicationMapper;

    public Page<ApplicationResponse> getApplicationsByProject(
            Long ownerId,
            Long projectId,
            Pageable pageable) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.withId(projectId));

        if (!project.getTeamLeadId().equals(ownerId)) {
            throw new ResourceAccessDeniedException("You have no access to this project as you are not teamlead");
        }

        return applicationRepository.findAllByProject(project, pageable)
                .map(applicationMapper::toDto);
    }

    public Page<ApplicationResponse> getApplicationsByMember(Long memberId, Pageable pageable) {
        Page<ApplicationResponse> page = applicationRepository.findAllByPersonId(memberId, pageable)
                .map(applicationMapper::toDto);

        return page;
    }

    @Transactional
    public void createApplication(Long personId, ApplicationRequest request) {
        List<ApplicationStatus> activeStatuses = List.of(ApplicationStatus.PENDING, ApplicationStatus.APPROVED);

        boolean exists = applicationRepository.existsByProjectIdAndPersonIdAndStatusIn(
                request.projectId(), personId, activeStatuses
        );

        if (exists) {
            throw new IllegalStateException("You have already applied for this project.");
        }

        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> ProjectNotFoundException.withId(request.projectId()));

        if (project.getStatus() == ProjectStatus.COMPLETE) {
            throw new IllegalStateException("You cannot apply to a completed or cancelled project.");
        }

        if (project.getTeamLeadId().equals(personId)) {
            throw new IllegalStateException("You cannot apply for your own project.");
        }

        int approvedCount = applicationRepository.countApprovedApplicationsByProjectId(request.projectId());
        if (approvedCount >= project.getRequiredMembersCount()) {
            throw new IllegalStateException("This project has no free slots.");
        }

        Application application = new Application();
        application.setPersonId(personId);
        application.setProject(project);
        application.setStatus(ApplicationStatus.PENDING);
        application.setCreatedAt(LocalDateTime.now());

        applicationRepository.save(application);
    }

    @Transactional
    public void deleteApplication(Long userId, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.withId(projectId));

        Application application = applicationRepository.findByProjectIdAndPersonId(projectId, userId)
                .orElseThrow(() -> new ResourceAccessDeniedException("Application not found."));

        applicationRepository.delete(application);
    }

    @Transactional
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

        if (project.getStatus() == ProjectStatus.COMPLETE) {
            throw new IllegalStateException("Cannot update applications for a completed project.");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> ru.teamsync.projects.service.exception.ApplicationNotFoundException.withId(applicationId));

        application.setStatus(status);
        applicationRepository.save(application);

        if (status == ApplicationStatus.APPROVED) {
            int approvedCount = applicationRepository.countApprovedApplicationsByProjectId(projectId);
            if (approvedCount >= project.getRequiredMembersCount()) {
                throw new IllegalStateException("Cannot approve — project is already full.");
            }

            ru.teamsync.projects.entity.ProjectMemberId memberId = new ru.teamsync.projects.entity.ProjectMemberId(projectId, application.getPersonId());

            boolean alreadyMember = projectMemberRepository.existsById(memberId);
            if (!alreadyMember) {
                ru.teamsync.projects.entity.ProjectMember member = new ru.teamsync.projects.entity.ProjectMember();
                member.setId(memberId);
                projectMemberRepository.save(member);
            }
        }

        return applicationMapper.toDto(application);
    }

    public Page<ApplicationResponse> getApplicationsForProject(Long projectId, Long userId, Pageable pageable) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.withId(projectId));

        if (!project.getTeamLeadId().equals(userId)) {
            throw new ResourceAccessDeniedException("You cannot view applications for this project");
        }

        Page<Application> applications = applicationRepository.findAllByProject(project, pageable);

        return applications.map(applicationMapper::toDto);
    }
}