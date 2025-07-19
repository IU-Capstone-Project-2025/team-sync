package ru.teamsync.resume.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get current user's profile", description = "Returns the profile of the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                     content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<BaseResponse<ProfileResponse>> getCurrentUserProfile() {
        Long personId = securityContextService.getCurrentUserId();
        ProfileResponse response = profileService.getProfile(personId);
        return ResponseEntity.ok(BaseResponse.of(response));
    }

    @GetMapping("/{personId}")
    @Operation(summary = "Get profile by person ID", description = "Returns a student or professor profile by person ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                     content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content)
    })
    public ResponseEntity<BaseResponse<ProfileResponse>> getProfile(
            @PathVariable Long personId) {
        ProfileResponse response = profileService.getProfile(personId);
        return ResponseEntity.ok(BaseResponse.of(response));
    }

    @PutMapping("/student/{personId}")
    @Operation(summary = "Update student profile", description = "Updates a student profile; only accessible by the profile owner")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                     content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content)
    })
    public ResponseEntity<BaseResponse<Void>> updateStudentProfile(
            @PathVariable Long personId,
            @RequestBody UpdateStudentProfileRequest request) {

        Long currentUserId = securityContextService.getCurrentUserId();
        profileService.updateStudentProfile(personId, request, currentUserId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }

    @PutMapping("/professor/{personId}")
    @Operation(summary = "Update professor profile", description = "Updates a professor profile; only accessible by the profile owner")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                     content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content)
    })
    public ResponseEntity<BaseResponse<Void>> updateProfessorProfile(
            @PathVariable Long personId,
            @RequestBody UpdateProfessorProfileRequest request) {

        Long currentUserId = securityContextService.getCurrentUserId();
        profileService.updateProfessorProfile(personId, request, currentUserId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }
}
