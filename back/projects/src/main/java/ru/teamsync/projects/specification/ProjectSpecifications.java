package ru.teamsync.projects.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import ru.teamsync.projects.entity.Project;

import java.util.ArrayList;
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
            List<Predicate> predicates = new ArrayList<>();
            for (Long skillId : skillIds) {
                predicates.add(cb.isMember(skillId, root.get("skillIds")));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    public static Specification<Project> hasRoleIds(List<Long> roleIds) {
        return (root, query, cb) -> {
            if (roleIds == null || roleIds.isEmpty()) {
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            for (Long roleId : roleIds) {
                predicates.add(cb.isMember(roleId, root.get("roleIds")));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
