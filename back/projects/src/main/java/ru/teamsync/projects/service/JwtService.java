package ru.teamsync.projects.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.teamsync.projects.config.properties.JwtProperties;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateTokenWithInternalId(int internalId) {
        return Jwts.builder()
                .claim(jwtProperties.userIdClaim(), internalId)
                .issuer(jwtProperties.issuer())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.expirationTimeMs()))
                .signWith(jwtProperties.getSecurityKey())
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecurityKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Long extractUserId(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecurityKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("user_id", Long.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecurityKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

}
