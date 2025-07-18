package ru.teamsync.projects.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ru.teamsync.projects.client.EmbedderClient;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.mapper.ApplicationMapper;
import ru.teamsync.projects.mapper.ProjectMapper;
import ru.teamsync.projects.repository.ApplicationRepository;
import ru.teamsync.projects.repository.ProjectMemberRepository;
import ru.teamsync.projects.repository.CourseRepository;
import ru.teamsync.projects.repository.ProjectRepository;
import ru.teamsync.projects.repository.StudentProjectClickRepository;
import ru.teamsync.projects.service.ProjectService;
import ru.teamsync.projects.service.exception.ResourceAccessDeniedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class ProjectServiceTest {

    private ProjectService projectService;

    private CourseRepository courseRepositoryMock;


    private ApplicationRepository applicationRepositoryMock;
    private ApplicationMapper applicationMapperMock;
    private ProjectMemberRepository projectMemberRepositoryMock;
    private StudentProjectClickRepository studentProjectClickRepositoryMock;
    private EmbedderClient embedderClientMock;

    private ProjectRepository projectRepositoryMock;
    private ProjectMapper projectMapperMock;

    @BeforeEach
    public void init() {
        applicationRepositoryMock = Mockito.mock(ApplicationRepository.class);
        applicationMapperMock = Mockito.mock(ApplicationMapper.class);
        projectRepositoryMock = Mockito.mock(ProjectRepository.class);
        projectMapperMock = Mockito.mock(ProjectMapper.class);
        projectMemberRepositoryMock = Mockito.mock(ProjectMemberRepository.class);
        studentProjectClickRepositoryMock = Mockito.mock(StudentProjectClickRepository.class);

        projectService = new ProjectService(
                applicationRepositoryMock,
                courseRepositoryMock,
                projectRepositoryMock,
                projectMemberRepositoryMock,
                studentProjectClickRepositoryMock,
                embedderClientMock,
                applicationMapperMock,
                projectMapperMock);
    }

    @Test
    public void should_throwAccessDenied_when_notTeamLeadDeletesProject() {
        //Arrange
        Project project = new Project();
        project.setTeamLeadId(123L);

        when(projectRepositoryMock.findById(1L)).thenReturn(Optional.of(project));

        //Act, Assert
        assertThatThrownBy(
                () -> projectService.deleteProject(1L, 2L)
        ).isInstanceOf(ResourceAccessDeniedException.class);
    }

    @Test
    public void should_throwAccessDenied_when_notTeamLeadUpdatesProject() {
        //Arrange
        Project project = new Project();
        project.setTeamLeadId(123L);

        when(projectRepositoryMock.findById(1L)).thenReturn(Optional.of(project));

        //Act, Assert
        assertThatThrownBy(
                () -> projectService.updateProject(1L, null, 2L)
        ).isInstanceOf(ResourceAccessDeniedException.class);
    }

}

