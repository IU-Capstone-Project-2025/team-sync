package ru.teamsync.resume.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.teamsync.resume.dto.response.BaseResponse;
import ru.teamsync.resume.dto.response.SkillResponse;
import ru.teamsync.resume.dto.response.RoleResponse;
import ru.teamsync.resume.service.ProfileService;

@RestController
@AllArgsConstructor
@RequestMapping("/profile/student")
public class StudentProfileController {

    private final ProfileService profileService;

    @GetMapping("/{personId}/skills")
    public ResponseEntity<BaseResponse<Page<SkillResponse>>> getStudentSkills(
            @PathVariable Long personId,
            Pageable pageable) throws NotFoundException {
        Page<SkillResponse> skills = profileService.getStudentSkills(personId, pageable);
        return ResponseEntity.ok(BaseResponse.of(skills));
    }

    @GetMapping("/{personId}/roles")
    public ResponseEntity<BaseResponse<Page<RoleResponse>>> getStudentRoles(
            @PathVariable Long personId,
            Pageable pageable) throws NotFoundException {
        Page<RoleResponse> roles = profileService.getStudentRoles(personId, pageable);
        return ResponseEntity.ok(BaseResponse.of(roles));
    }
}
