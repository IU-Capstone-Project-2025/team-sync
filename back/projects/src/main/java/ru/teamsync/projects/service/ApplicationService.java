package ru.teamsync.projects.service;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.teamsync.projects.dto.request.ApplicationRequest;
import ru.teamsync.projects.dto.response.ApplicationResponse;
import ru.teamsync.projects.entity.Application;
import ru.teamsync.projects.entity.ApplicationStatus;
import ru.teamsync.projects.mapper.ApplicationMapper;
import ru.teamsync.projects.repository.ApplicationRepository;

@Service
@AllArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    /*
    public Page<ApplicationResponse> getApplicationsByProject(Long projectId, Pageable pageable) {
        return applicationRepository.findAllByProjectId(projectId, pageable)
                .map(applicationMapper::toDto);
    }
    */

    public Page<ApplicationResponse> getApplicationsByMember(Long memberId, Pageable pageable) {
        return applicationRepository.findAllByMemberId(memberId, pageable)
                .map(applicationMapper::toDto);
    }

    public void createApplication(Long memberId, ApplicationRequest request) {
        Application application = new Application();
        application.setMemberId(memberId);
        application.setId(request.projectId());
        application.setStatus(ApplicationStatus.PENDING);
        application.setCreatedAt(LocalDateTime.now());

        applicationRepository.save(application);
    }
}
