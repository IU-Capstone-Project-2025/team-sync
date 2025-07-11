package ru.teamsync.resume.controller;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import ru.teamsync.resume.dto.response.BaseResponse;
import ru.teamsync.resume.dto.response.ProfileResponse;
import ru.teamsync.resume.service.ProfileService;
import ru.teamsync.resume.service.SecurityContextService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ru.teamsync.resume.dto.request.UpdateProfessorProfileRequest;
import ru.teamsync.resume.dto.request.UpdateStudentProfileRequest;


@RestController
@AllArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final SecurityContextService securityContextService;

    @GetMapping
    public ResponseEntity<BaseResponse<ProfileResponse>> getCurrentUserProfile() {
        Long personId = securityContextService.getCurrentUserId();
        ProfileResponse response = profileService.getProfile(personId);
        return ResponseEntity.ok(BaseResponse.of(response));
    }

    @GetMapping("/{personId}")
    public ResponseEntity<BaseResponse<ProfileResponse>> getProfile(
            @PathVariable Long personId) {
        ProfileResponse response = profileService.getProfile(personId);
        return ResponseEntity.ok(BaseResponse.of(response));
    }

    @PutMapping("/student/{personId}")
    public ResponseEntity<BaseResponse<Void>> updateStudentProfile(
            @PathVariable Long personId,
            @RequestBody UpdateStudentProfileRequest request) {

        Long currentUserId = securityContextService.getCurrentUserId();
        profileService.updateStudentProfile(personId, request, currentUserId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }

    @PutMapping("/professor/{personId}")
    public ResponseEntity<BaseResponse<Void>> updateProfessorProfile(
            @PathVariable Long personId,
            @RequestBody UpdateProfessorProfileRequest request) {

        Long currentUserId = securityContextService.getCurrentUserId();
        profileService.updateProfessorProfile(personId, request, currentUserId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }
}
