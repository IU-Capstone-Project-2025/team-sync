package ru.teamsync.auth.config.properties;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@ConfigurationProperties(prefix = "teamsync.security.jwt")
public record JwtProperties(
        String base64Key,
        Long expirationTimeMs,
        String issuer,
        String userIdClaim,
        String profileIdClaim
) {

    public Key getSecurityKey() {
        byte[] keyBytes = base64Key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
