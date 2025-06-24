package ru.teamsync.resume.dto.request;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateProfileRequest(
    @NotNull Long person_id,
    @NotNull @Pattern(regexp = "student|professor") String type,
    @NotNull Profile profile
) {
    public sealed interface Profile permits StudentProfile, ProfessorProfile {}

    public record StudentProfile(
        @NotBlank String name,
        @NotBlank String surname,
        @Email String email,
        @NotBlank String study_group,
        String description,
        @NotBlank String github_alias,
        @NotBlank String tg_alias,
        List<Long> skills,
        List<Long> roles
    ) implements Profile {}

    public record ProfessorProfile(
        @NotBlank String name,
        @NotBlank String surname,
        @Email String email,
        String tg_alias
    ) implements Profile {}
}
