package ru.teamsync.resume.dto.request;

import ru.teamsync.resume.entity.StudyGroup;

import java.util.List;

public record UpdateStudentProfileRequest(
    StudyGroup studyGroup,
    String description,
    String githubAlias,
    String tgAlias,
    List<Long> skills,
    List<Long> roles
) {}
