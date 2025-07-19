package ru.teamsync.projects.service.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.teamsync.projects.config.properties.JwtProperties;

@Service
@RequiredArgsConstructor
@Log4j2
public class JwtService {

    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;

    public SecurityUser extractSecurityUser(String token) {
        var claims = Jwts.parser()
                .setSigningKey(jwtProperties.getSecurityKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return objectMapper.convertValue(claims, SecurityUser.class);
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
