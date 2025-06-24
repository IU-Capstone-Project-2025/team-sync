package ru.teamsync.resume.dto.response;

import java.util.List;

import ru.teamsync.resume.entity.StudyGroup;

public record StudentProfileResponse(
    StudyGroup studyGroup,
    String description,
    String githubAlias,
    String tgAlias,
    List<Long> skills,
    List<Long> roles
) {}