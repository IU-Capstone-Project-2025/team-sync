package ru.teamsync.projects.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.entity.Skill;
import ru.teamsync.projects.entity.Role;

import java.util.List;

public class ProjectSpecifications {
    public static Specification<Project> hasCourseName(String courseName) {
        return (root, query, cb) -> {
            if (courseName == null || courseName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("courseName"), courseName);
        };
    }

    public static Specification<Project> hasSkillIds(List<Long> skillIds) {
        return (root, query, cb) -> {
            if (skillIds == null || skillIds.isEmpty()) {
                return cb.conjunction();
            }
            Join<Project, Skill> skills = root.join("skills", JoinType.LEFT);
            return skills.get("id").in(skillIds);
        };
    }

    public static Specification<Project> hasRoleIds(List<Long> roleIds) {
        return (root, query, cb) -> {
            if (roleIds == null || roleIds.isEmpty()) {
                return cb.conjunction();
            }
            Join<Project, Role> roles = root.join("roles", JoinType.LEFT);
            return roles.get("id").in(roleIds);
        };
    }
}
