package ru.teamsync.resume.controller;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import ru.teamsync.resume.dto.response.BaseResponse;
import ru.teamsync.resume.dto.response.ProfileResponse;
import ru.teamsync.resume.service.ProfileService;

@RestController
@AllArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/{person_id}")
    public ResponseEntity<BaseResponse<ProfileResponse>> getProfile(@PathVariable Long personId) throws NotFoundException {
        try {
            ProfileResponse response = profileService.getProfile(personId);
            return ResponseEntity.ok(BaseResponse.of(response));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(BaseResponse.withErrorMessage(e.getMessage()));
        }
    }
}
