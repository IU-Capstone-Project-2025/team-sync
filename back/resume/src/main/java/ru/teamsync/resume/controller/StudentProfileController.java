package ru.teamsync.resume.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import ru.teamsync.resume.dto.request.UpdateStudentProfileRequest;
import ru.teamsync.resume.dto.response.BaseResponse;
import ru.teamsync.resume.dto.response.SkillResponse;
import ru.teamsync.resume.dto.response.RoleResponse;
import ru.teamsync.resume.service.ProfileService;

@RestController
@AllArgsConstructor
@RequestMapping("/profile/student")
public class StudentProfileController {

    private final ProfileService profileService;

    @PutMapping("/student/{personId}")
    public ResponseEntity<BaseResponse<Void>> updateStudentProfile(
            @PathVariable Long personId, 
            @RequestBody UpdateStudentProfileRequest request,
            @AuthenticationPrincipal Jwt jwt) throws NotFoundException, AccessDeniedException {

        Long currentUserId = jwt.getClaim("internal_id");
        profileService.updateStudentProfile(personId, request, currentUserId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }

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
