package ru.teamsync.projects.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.entity.ProjectMemberId;
import ru.teamsync.projects.repository.ProjectMemberRepository;
import ru.teamsync.projects.repository.ProjectRepository;
import ru.teamsync.projects.service.exception.ProjectNotFoundException;
import ru.teamsync.projects.service.exception.ResourceAccessDeniedException;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

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

}
