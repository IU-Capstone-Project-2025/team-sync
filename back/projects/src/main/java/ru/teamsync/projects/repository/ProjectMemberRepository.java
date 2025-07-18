package ru.teamsync.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.teamsync.projects.entity.ProjectMember;
import ru.teamsync.projects.entity.ProjectMemberId;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    long countById_ProjectId(Long projectId);
}