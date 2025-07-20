package ru.teamsync.resume.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.teamsync.resume.dto.response.SkillResponse;
import ru.teamsync.resume.service.SkillService;

@RestController
@RequestMapping("/skills")
@AllArgsConstructor
@Tag(name = "Skills", description = "Provides access to skills")
public class SkillController {

    private final SkillService skillService;

    @Operation(
        summary = "Get list of skills",
        description = "Returns paginated list of skills filtered by search query (optional)"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved skills")
    @GetMapping
    public ResponseEntity<Page<SkillResponse>> getSkills(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        Page<SkillResponse> skills = skillService.getSkills(search, pageable);
        return ResponseEntity.ok(skills);
    }
}