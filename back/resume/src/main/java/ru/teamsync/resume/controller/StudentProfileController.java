package ru.teamsync.resume.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.teamsync.resume.dto.response.BaseResponse;
import ru.teamsync.resume.dto.response.SkillResponse;
import ru.teamsync.resume.dto.response.RoleResponse;
import ru.teamsync.resume.service.ProfileService;

@RestController
@AllArgsConstructor
@RequestMapping("/profile/student")
@Tag(name = "Student Profiles", description = "Manage student profiles, including their skills and roles")
public class StudentProfileController {

    private final ProfileService profileService;

    @Operation(
        summary = "Get student's skills",
        description = "Returns a paginated list of student's selected skills"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved student skills")
    @GetMapping("/{personId}/skills")
    public ResponseEntity<BaseResponse<Page<SkillResponse>>> getStudentSkills(
            @PathVariable Long personId,
            Pageable pageable) throws NotFoundException {
        Page<SkillResponse> skills = profileService.getStudentSkills(personId, pageable);
        return ResponseEntity.ok(BaseResponse.of(skills));
    }

    @Operation(
        summary = "Get student's preferred roles",
        description = "Returns a paginated list of roles selected by the student"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved student roles")
    @GetMapping("/{personId}/roles")
    public ResponseEntity<BaseResponse<Page<RoleResponse>>> getStudentRoles(
            @PathVariable Long personId,
            Pageable pageable) throws NotFoundException {
        Page<RoleResponse> roles = profileService.getStudentRoles(personId, pageable);
        return ResponseEntity.ok(BaseResponse.of(roles));
    }
}
