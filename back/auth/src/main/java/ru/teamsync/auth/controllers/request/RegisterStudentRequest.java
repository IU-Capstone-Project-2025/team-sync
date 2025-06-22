package ru.teamsync.auth.controllers.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterStudentRequest(
        @JsonProperty("study_group")
        String studyGroup,

        String description,

        @JsonProperty("github_alias")
        String githubAlias,

        @JsonProperty("tg_alias")
        String tgAlias
) {
}
