package ru.teamsync.auth.services.jwt;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.teamsync.auth.config.properties.JwtProperties;

import java.util.Date;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class JwtService {

    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFENCE = new TypeReference<>() {
    };

    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;

    public String generateToken(JwtUserClaims claimsDto) {
        Map<String, Object> claims = objectMapper.convertValue(claimsDto, MAP_TYPE_REFENCE);
        log.info("Profile information: {}", claimsDto);
        log.info("Generating JWT token for claims: {}", claims);
        return Jwts.builder()
                .claims(claims)
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
}
