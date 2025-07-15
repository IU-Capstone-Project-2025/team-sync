package ru.teamsync.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.teamsync.projects.entity.ProjectMember;
import ru.teamsync.projects.entity.ProjectMemberId;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    boolean existsById_ProjectIdAndId_MemberId(Long projectId, Long memberId);
    void deleteById_ProjectIdAndId_MemberId(Long projectId, Long memberId);
    long countById_ProjectId(Long projectId);
}