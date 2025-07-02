package ru.teamsync.resume.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateProfessorProfileRequest(
    @JsonProperty("tg_alias")
    String tgAlias
) {}