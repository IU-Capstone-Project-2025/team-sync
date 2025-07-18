package ru.teamsync.resume.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.teamsync.resume.config.properties.JwtProperties;

@Service
@RequiredArgsConstructor
@Log4j2
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
            log.error(e.getMessage());
            return false;
        }
    }

}
