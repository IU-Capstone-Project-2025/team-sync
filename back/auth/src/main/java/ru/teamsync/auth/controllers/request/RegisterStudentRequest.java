package ru.teamsync.auth.controllers.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RegisterStudentRequest(
        @JsonProperty("study_group")
        String studyGroup,

        String description,

        @JsonProperty("github_alias")
        String githubAlias,

        @JsonProperty("tg_alias")
        String tgAlias,

        List<Long> skills,

        List<Long> roles
) {
}
