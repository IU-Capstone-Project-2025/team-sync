package ru.teamsync.auth.controllers.request;


import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterProfessorRequest(
        @JsonProperty("tg_alias")
        String tgAlias
) {
}
