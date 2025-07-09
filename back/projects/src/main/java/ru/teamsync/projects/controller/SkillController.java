
package ru.teamsync.projects.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.entity.Skill;
import ru.teamsync.projects.service.SkillService;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public BaseResponse<Page<Skill>> getSkills(Pageable pageable) {
        return BaseResponse.of(skillService.getSkills(pageable));
    }
}
