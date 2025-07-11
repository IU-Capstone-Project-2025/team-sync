package ru.teamsync.auth.services;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.teamsync.auth.config.properties.JwtProperties;

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

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecurityKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /*@PostConstruct
    public void printTestToken() {
        String token = generateTokenWithInternalId(123);
        System.out.println("Internal test token: " + token);
    } */
}
