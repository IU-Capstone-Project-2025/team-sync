package ru.teamsync.auth.services.jwt;

import ru.teamsync.auth.config.security.userdetails.Role;

import java.util.List;

public record JwtUserClaims(
        Integer userId,
        Integer profileId,
        List<Role> roles,
        String email
) {

}
