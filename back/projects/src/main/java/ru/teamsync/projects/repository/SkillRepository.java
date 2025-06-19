package ru.teamsync.projects.repository;

import ru.teamsync.projects.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long>{
}
