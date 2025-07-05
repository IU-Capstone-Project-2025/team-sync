package ru.teamsync.projects.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.entity.Role;
import ru.teamsync.projects.entity.Skill;

import java.util.List;

public class ProjectSpecifications {
    public static Specification<Project> hasAnyCourseIds(List<Long> courseIds) {
        return (root, query, cb) -> {
            if (courseIds == null || courseIds.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("course").get("id").in(courseIds);
        };
    }


    public static Specification<Project> hasSkillIds(List<Long> skillIds) {
        return (root, query, cb) -> {
            if (skillIds == null || skillIds.isEmpty()) {
                return cb.conjunction();
            }
            Join<Project, Skill> projectSkillJoin = root.join("skillIds");
            return projectSkillJoin.in(skillIds);
        };
    }

    public static Specification<Project> hasRoleIds(List<Long> roleIds) {
        return (root, query, cb) -> {
            if (roleIds == null || roleIds.isEmpty()) {
                return cb.conjunction();
            }
            Join<Project, Role> projectRoleJoin = root.join("roleIds");
            return projectRoleJoin.in(roleIds);
        };
    }
}
