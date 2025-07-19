
package ru.teamsync.projects.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.entity.Skill;
import ru.teamsync.projects.service.SkillService;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
@Tag(name = "Skills", description = "Retrieve skills available and used in projects")
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    @Operation(summary = "Get all skills", description = "Returns a paginated list of all available skills")
    public BaseResponse<Page<Skill>> getSkills(Pageable pageable) {
        return BaseResponse.of(skillService.getSkills(pageable));
    }

    @GetMapping("/in-projects")
    @Operation(summary = "Get skills in projects", description = "Returns a paginated list of skills currently used in projects")
    public BaseResponse<Page<Skill>> getSkillsInProject(Pageable pageable) {
        return BaseResponse.of(skillService.getSkillsInProjects(pageable));
    }
}
