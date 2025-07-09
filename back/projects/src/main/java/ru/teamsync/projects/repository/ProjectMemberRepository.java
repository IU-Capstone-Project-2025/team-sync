package ru.teamsync.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.teamsync.projects.entity.ProjectMember;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    boolean existsByProjectIdAndMemberId(Long projectId, Long memberId);
}