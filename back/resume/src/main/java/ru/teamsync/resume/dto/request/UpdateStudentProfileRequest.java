package ru.teamsync.resume.dto.request;

import ru.teamsync.resume.entity.StudyGroup;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateStudentProfileRequest(

    @JsonProperty("study_gtoup")
    StudyGroup studyGroup,
    String description,

    @JsonProperty("github_alias")
    String githubAlias,

    @JsonProperty("tg_alias")
    String tgAlias,

    List<Long> skills,
    List<Long> roles
) {}
