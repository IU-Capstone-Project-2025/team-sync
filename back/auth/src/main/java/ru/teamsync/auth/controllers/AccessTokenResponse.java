package ru.teamsync.auth.controllers;

import lombok.Value;

public record AccessTokenResponse(
        String accessToken
) {
}
