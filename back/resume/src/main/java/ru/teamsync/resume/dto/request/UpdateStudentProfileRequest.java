package ru.teamsync.resume.dto.request;

import java.util.List;

public record UpdateStudentProfileRequest(
    String study_group,
    String description,
    String github_alias,
    String tg_alias,
    List<Long> skills,
    List<Long> roles
) {}
