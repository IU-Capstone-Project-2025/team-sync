package ru.teamsync.projects.service.security;

import java.util.List;

public record SecurityUser(
        Long userId,
        Long profileId,
        List<SecurityRole> roles,
        String email
) {
}
