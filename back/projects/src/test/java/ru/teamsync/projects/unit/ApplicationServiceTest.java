package ru.teamsync.projects.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.teamsync.projects.dto.request.ApplicationRequest;
import ru.teamsync.projects.entity.Application;
import ru.teamsync.projects.entity.ApplicationStatus;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.mapper.ApplicationMapper;
import ru.teamsync.projects.repository.ApplicationRepository;
import ru.teamsync.projects.repository.ProjectRepository;
import ru.teamsync.projects.service.ApplicationService;
import ru.teamsync.projects.service.exception.ResourceAccessDeniedException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ApplicationServiceTest {

    private ApplicationService applicationService;
    private ProjectRepository projectRepositoryMock;
    private ApplicationRepository applicationRepositoryMock;
    private ApplicationMapper applicationMapperMock;

    @BeforeEach
    public void init() {
        projectRepositoryMock = Mockito.mock(ProjectRepository.class);
        applicationRepositoryMock = Mockito.mock(ApplicationRepository.class);
        applicationMapperMock = Mockito.mock(ApplicationMapper.class);

        applicationService = new ApplicationService(projectRepositoryMock, applicationRepositoryMock, applicationMapperMock);
    }

    @Test
    public void should_throwAccessDenied_when_notTeamLeadGettingApplications() throws ChangeSetPersister.NotFoundException {
        //Arrange
        Project project = new Project();
        project.setTeamLeadId(123L);
        Page<Project> page = new PageImpl(List.of(project));

        when(projectRepositoryMock.findAllByTeamLeadId(any(), any())).thenReturn(page);

        //Act, Assert
        assertThatThrownBy(
                () -> applicationService.getApplicationsByProject(1L, 2L, null)
        ).isInstanceOf(ResourceAccessDeniedException.class);
    }

    @Test
    public void should_setStatusPending_when_savingNewApplication() {
        //Arrange
        ApplicationRequest applicationRequest = new ApplicationRequest(123L);

        //Act
        applicationService.createApplication(1L, applicationRequest);

        //Assert
        ArgumentCaptor<Application> actualApplicationCaptor = ArgumentCaptor.forClass(Application.class);
        verify(applicationRepositoryMock).save(actualApplicationCaptor.capture());
        Application actualApplication = actualApplicationCaptor.getValue();

        assertThat(actualApplication.getStatus()).isEqualTo(ApplicationStatus.PENDING);
    }

}
