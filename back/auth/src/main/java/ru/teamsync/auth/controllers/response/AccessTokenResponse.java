package ru.teamsync.auth.controllers.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessTokenResponse(

        @JsonProperty("access_token")
        String accessToken
) {
}
