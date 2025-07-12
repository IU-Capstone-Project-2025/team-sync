package ru.teamsync.projects.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.teamsync.projects.entity.Skill;
import ru.teamsync.projects.repository.SkillRepository;

@Service
@AllArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;

    public Page<Skill> getSkills(Pageable pageable) {
        return skillRepository.findAll(pageable);
    }

    public Page<Skill> getSkillsInProjects(Pageable pageable) {
        return skillRepository.findSkillsInProjects(pageable);
    }
}
