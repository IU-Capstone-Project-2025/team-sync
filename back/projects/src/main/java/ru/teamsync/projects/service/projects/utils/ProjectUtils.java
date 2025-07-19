package ru.teamsync.projects.service.projects.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.teamsync.projects.dto.response.ProjectResponse;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.mapper.ProjectMapper;
import ru.teamsync.projects.repository.ProjectMemberRepository;
import ru.teamsync.projects.service.projects.FiltrationParameters;

@Service
@RequiredArgsConstructor
public class ProjectUtils {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectMapper projectMapper;

    public ProjectResponse enrichWithMemberCount(ProjectResponse project) {
        int count = (int) projectMemberRepository.countById_ProjectId(project.getId());
        project.setMembersCount(count);
        return project;
    }

    public Specification<Project> buildSpecification(FiltrationParameters filterParams) {
        Specification<Project> spec = Specification.where(null);
        if (filterParams.courseIds() != null && !filterParams.courseIds().isEmpty()) {
            spec = spec.and(ProjectSpecifications.hasAnyCourseIds(filterParams.courseIds()));
        }
        if (filterParams.skillIds() != null && !filterParams.skillIds().isEmpty()) {
            spec = spec.and(ProjectSpecifications.hasSkillIds(filterParams.skillIds()));
        }
        if (filterParams.roleIds() != null && !filterParams.roleIds().isEmpty()) {
            spec = spec.and(ProjectSpecifications.hasRoleIds(filterParams.roleIds()));
        }
        if (filterParams.status() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), filterParams.status()));
        }
        return spec;
    }

}
