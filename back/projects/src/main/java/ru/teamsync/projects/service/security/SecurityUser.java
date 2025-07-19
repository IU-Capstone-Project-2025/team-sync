package ru.teamsync.projects.service.jwt;

import java.util.List;

public record SecurityUser(
        Integer internalId,
        Integer profileId,
        List<SecurityRole> roles,
        String email
) {
}
