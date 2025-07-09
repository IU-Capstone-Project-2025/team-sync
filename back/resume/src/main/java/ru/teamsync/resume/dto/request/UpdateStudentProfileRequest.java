package ru.teamsync.resume.dto.request;

import ru.teamsync.resume.entity.StudyGroup;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateStudentProfileRequest(

    String studyGroup,

    String description,

    String githubAlias,

    String tgAlias,

    List<Long> skills,
    List<Long> roles
) {}
