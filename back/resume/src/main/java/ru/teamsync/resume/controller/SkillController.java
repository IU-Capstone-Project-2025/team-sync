package ru.teamsync.resume.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.teamsync.resume.dto.response.SkillResponse;
import ru.teamsync.resume.service.SkillService;

@RestController
@RequestMapping("/skills")
@AllArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<Page<SkillResponse>> getSkills(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        Page<SkillResponse> skills = skillService.getSkills(search, pageable);
        return ResponseEntity.ok(skills);
    }
}