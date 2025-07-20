package ru.teamsync.projects.service.projects;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.teamsync.projects.client.EmbedderClient;
import ru.teamsync.projects.dto.request.ProjectCreateRequest;
import ru.teamsync.projects.dto.request.ProjectUpdateRequest;
import ru.teamsync.projects.dto.response.ProjectResponse;
import ru.teamsync.projects.entity.Course;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.entity.ProjectStatus;
import ru.teamsync.projects.entity.StudentProjectClick;
import ru.teamsync.projects.entity.StudentProjectClickId;
import ru.teamsync.projects.mapper.ProjectMapper;
import ru.teamsync.projects.repository.CourseRepository;
import ru.teamsync.projects.repository.ProjectRepository;
import ru.teamsync.projects.repository.StudentProjectClickRepository;
import ru.teamsync.projects.service.exception.ProjectNotFoundException;
import ru.teamsync.projects.service.exception.ResourceAccessDeniedException;
import ru.teamsync.projects.service.projects.utils.ProjectUtils;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final CourseRepository courseRepository;
    private final ProjectRepository projectRepository;
    private final StudentProjectClickRepository studentProjectClickRepository;
    private final EmbedderClient embedderClient;
    private final ProjectUtils projectUtils;

    private final ProjectMapper projectMapper;

    @Transactional
    public void createProject(ProjectCreateRequest request, Long userId) {
        Project project = projectMapper.toEntity(request, userId);

        Course course = courseRepository.findByName(request.courseName())
                .orElseGet(() -> {
                    Course newCourse = new Course();
                    newCourse.setName(request.courseName());
                    return courseRepository.save(newCourse);
                });
        project.setCourse(course);
        projectRepository.save(project);
        embedderClient.recalculateProjectPoints(project.getId());
    }

    @Transactional
    public void updateProject(Long projectId, ProjectUpdateRequest request, Long currentUserId) throws NotFoundException, AccessDeniedException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.withId(projectId));

        if (!project.getTeamLeadId().equals(currentUserId)) {
            throw new ResourceAccessDeniedException("You cannot edit this project");
        }

        if (project.getStatus() == ProjectStatus.COMPLETE) {
            throw new IllegalStateException("Cannot update a completed project.");
        }

        Course course = courseRepository.findByName(request.courseName())
                .orElseGet(() -> {
                    Course newCourse = new Course();
                    newCourse.setName(request.courseName());
                    return courseRepository.save(newCourse);
                });

        projectMapper.updateEntity(request, project);
        project.setCourse(course);
        projectRepository.save(project);
        if (request.description() != null) {
            embedderClient.recalculateProjectPoints(project.getId());
        }
    }

    public Page<ProjectResponse> getProjects(FiltrationParameters filterParams, Pageable pageable) {

        Specification<Project> spec = projectUtils.buildSpecification(filterParams);

        Page<Project> projects = projectRepository.findAll(spec, pageable);
        return projects.map(projectMapper::toDto).map(projectUtils::enrichWithMemberCount);
    }

    public ProjectResponse getProjectById(Long userId, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.withId(projectId));

        incrementProjectClick(userId, projectId);

        return projectUtils.enrichWithMemberCount(projectMapper.toDto(project));
    }

    public Page<ProjectResponse> getProjectsByTeamLead(Long teamLeadId, Pageable pageable) {
        Page<Project> projects = projectRepository.findAllByTeamLeadId(teamLeadId, pageable);

        return projects.map(projectMapper::toDto).map(projectUtils::enrichWithMemberCount);
    }

    @Transactional
    public void deleteProject(Long projectId, Long currentUserId) throws SecurityException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.withId(projectId));

        if (!project.getTeamLeadId().equals(currentUserId)) {
            throw new ResourceAccessDeniedException("You cannot delete this project");
        }

        projectRepository.delete(project);
    }

    @Transactional
    private void incrementProjectClick(Long studentId, Long projectId) {
        StudentProjectClickId clickId = new StudentProjectClickId(studentId, projectId);
        StudentProjectClick click = studentProjectClickRepository.findById(clickId)
                .orElseGet(() -> {
                    StudentProjectClick newClick = new StudentProjectClick();
                    newClick.setId(clickId);
                    return studentProjectClickRepository.save(newClick);
                });

        click.setClickCount(click.getClickCount() + 1);
        studentProjectClickRepository.save(click);
    }


}