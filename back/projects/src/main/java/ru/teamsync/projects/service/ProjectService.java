package ru.teamsync.projects.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ru.teamsync.projects.dto.request.ProjectCreateRequest;
import ru.teamsync.projects.dto.request.ProjectUpdateRequest;
import ru.teamsync.projects.dto.response.ApplicationResponse;
import ru.teamsync.projects.dto.response.ProjectResponse;
import ru.teamsync.projects.entity.Application;
import ru.teamsync.projects.entity.ApplicationStatus;
import ru.teamsync.projects.entity.Course;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.entity.ProjectMember;
import ru.teamsync.projects.entity.ProjectMemberId;
import ru.teamsync.projects.entity.ProjectStatus;
import ru.teamsync.projects.entity.StudentProjectClick;
import ru.teamsync.projects.entity.StudentProjectClickId;
import ru.teamsync.projects.mapper.ApplicationMapper;
import ru.teamsync.projects.mapper.ProjectMapper;
import ru.teamsync.projects.repository.ApplicationRepository;
import ru.teamsync.projects.repository.ProjectMemberRepository;
import ru.teamsync.projects.repository.CourseRepository;
import ru.teamsync.projects.repository.ProjectRepository;
import ru.teamsync.projects.repository.StudentProjectClickRepository;
import ru.teamsync.projects.service.exception.ApplicationNotFoundException;
import ru.teamsync.projects.service.exception.ProjectNotFoundException;
import ru.teamsync.projects.service.exception.ResourceAccessDeniedException;
import ru.teamsync.projects.specification.ProjectSpecifications;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ApplicationRepository applicationRepository;

    private final CourseRepository courseRepository;

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final StudentProjectClickRepository studentProjectClickRepository;
    
    private final ApplicationMapper applicationMapper;
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
    }

    public void removeMembersFromProject(Long projectId, Long currentUserId, Long personId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.withId(projectId));

        if (!project.getTeamLeadId().equals(currentUserId)) {
            throw new ResourceAccessDeniedException("You cannot edit this project");
        }

        ProjectMemberId memberId = new ProjectMemberId(projectId, personId);
        boolean exists = projectMemberRepository.existsById(memberId);
        if (!exists) {
            throw new IllegalStateException("This person is not a member of the project.");
        }

        projectMemberRepository.deleteById(memberId);
    }

    public Page<ProjectResponse> getProjects(
            List<Long> skillIds, 
            List<Long> roleIds, 
            List<Long> courseIds, 
            ProjectStatus status, 
            Pageable pageable) {
    
        Specification<Project> spec = Specification.where(null);

        if (courseIds != null && !courseIds.isEmpty()) {
            spec = spec.and(ProjectSpecifications.hasAnyCourseIds(courseIds));
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

        Page<Project> projects = projectRepository.findAll(spec, pageable);
        return projects.map(this::enrichWithMemberCount);
    }

    public ProjectResponse getProjectById(Long userId, Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> ProjectNotFoundException.withId(projectId));

        incrementProjectClick(userId, projectId);

        return enrichWithMemberCount(project);
    }

    public Page<ProjectResponse> getProjectsByTeamLead(Long teamLeadId, Pageable pageable) {
        Page<Project> projects = projectRepository.findAllByTeamLeadId(teamLeadId, pageable);

        return projects.map(this::enrichWithMemberCount);
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

    public Page<ApplicationResponse> getApplicationsForProject(Long projectId, Long userId, Pageable pageable) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.withId(projectId));

        if (!project.getTeamLeadId().equals(userId)) {
            throw new ResourceAccessDeniedException("You cannot view applications for this project");
        }

        Page<Application> applications = applicationRepository.findAllByProject(project, pageable);

        return applications.map(applicationMapper::toDto);
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
                .orElseThrow(() -> ApplicationNotFoundException.withId(applicationId));

        application.setStatus(status);
        applicationRepository.save(application);

        if (status == ApplicationStatus.APPROVED) {
            int approvedCount = applicationRepository.countApprovedApplicationsByProjectId(projectId);
            if (approvedCount >= project.getRequiredMembersCount()) {
                throw new IllegalStateException("Cannot approve — project is already full.");
            }

            ProjectMemberId memberId = new ProjectMemberId(projectId, application.getPersonId());

            boolean alreadyMember = projectMemberRepository.existsById(memberId);
            if (!alreadyMember) {
                ProjectMember member = new ProjectMember();
                member.setId(memberId);
                projectMemberRepository.save(member);
            }
        }

        return applicationMapper.toDto(application);
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

    private ProjectResponse enrichWithMemberCount(Project project) {
        int count = (int) projectMemberRepository.countById_ProjectId(project.getId());
        ProjectResponse dto = projectMapper.toDto(project);
        return new ProjectResponse(
            dto.id(),
            dto.name(),
            dto.courseId(),
            dto.teamLeadId(),
            dto.description(),
            dto.projectLink(),
            dto.status(),
            dto.skillIds(),
            dto.roleIds(),
            dto.requiredMembersCount(),
            count
        );
    }
}