package ru.teamsync.projects.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.entity.Skill;
import ru.teamsync.projects.service.SkillService;

@RestController
@RequestMapping("/skills")
@AllArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @GetMapping
    public BaseResponse<Page<Skill>> getSkills(Pageable pageable) {
        return new BaseResponse<>(skillService.getSkills(pageable), true, null);
    }
}
