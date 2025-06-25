package ru.teamsync.resume.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import ru.teamsync.resume.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long>{
    @Override
    @NonNull
    Page<Skill> findAll(@NonNull Pageable pageable);
    Page<Skill> findByIdIn(List<Long> ids, Pageable pageable);

    Page<Skill> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
