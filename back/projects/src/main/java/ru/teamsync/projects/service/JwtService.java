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

    public Long extractUserId(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecurityKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(jwtProperties.userIdClaim(), Long.class);
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
