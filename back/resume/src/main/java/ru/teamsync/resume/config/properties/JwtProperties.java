package ru.teamsync.resume.config.properties;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@ConfigurationProperties(prefix = "teamsync.security.jwt")
public record JwtProperties(
        String base64Key,
        Long expirationTimeMs,
        String issuer,
        String userIdClaim
) {

    public Key getSecurityKey() {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
