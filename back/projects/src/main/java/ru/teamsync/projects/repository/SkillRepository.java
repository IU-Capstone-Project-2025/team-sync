package ru.teamsync.projects.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import ru.teamsync.projects.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SkillRepository extends JpaRepository<Skill, Long>{
    @Override
    @NonNull
    Page<Skill> findAll(@NonNull Pageable pageable);

    @Query(value = "SELECT DISTINCT s.* FROM skill s JOIN project_skill ps ON s.id = ps.skill_id",
            countQuery = "SELECT COUNT(DISTINCT ps.skill_id) FROM project_skill ps",
            nativeQuery = true)
    Page<Skill> findSkillsInProjects(Pageable pageable);
}
