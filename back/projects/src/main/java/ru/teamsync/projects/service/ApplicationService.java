package ru.teamsync.projects.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import ru.teamsync.projects.dto.request.ApplicationRequest;
import ru.teamsync.projects.dto.response.ApplicationResponse;
import ru.teamsync.projects.entity.Application;
import ru.teamsync.projects.entity.ApplicationStatus;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.mapper.ApplicationMapper;
import ru.teamsync.projects.repository.ApplicationRepository;
import ru.teamsync.projects.repository.ProjectRepository;
import ru.teamsync.projects.service.exception.ProjectNotFoundException;
import ru.teamsync.projects.service.exception.ResourceAccessDeniedException;

@Service
@AllArgsConstructor
public class ApplicationService {
    private final ProjectRepository projectRepository;
    private final ApplicationRepository applicationRepository;
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

        return applicationRepository.findAllByProjectId(projectId, pageable)
                .map(applicationMapper::toDto);
    }

    public Page<ApplicationResponse> getApplicationsByMember(Long memberId, Pageable pageable) {
        return applicationRepository.findAllByStudentId(memberId, pageable)
                .map(applicationMapper::toDto);
    }

    public void createApplication(Long studentId, ApplicationRequest request) {
        Application application = new Application();
        application.setStudentId(studentId);
        application.setId(request.projectId());
        application.setStatus(ApplicationStatus.PENDING);
        application.setCreatedAt(LocalDateTime.now());

        applicationRepository.save(application);
    }
}
