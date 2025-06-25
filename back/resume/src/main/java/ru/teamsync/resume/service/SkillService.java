package ru.teamsync.resume.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.teamsync.resume.dto.response.SkillResponse;
import ru.teamsync.resume.entity.Skill;
import ru.teamsync.resume.repository.SkillRepository;

@Service
@AllArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;

    public Page<SkillResponse> getSkills(String search, Pageable pageable) {
        Page<Skill> skills;
        if (search != null && !search.isBlank()) {
            skills = skillRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            skills = skillRepository.findAll(pageable);
        }
        return skills.map(skill -> new SkillResponse(skill.getId(), skill.getName(), skill.getDescription()));
    }
}
