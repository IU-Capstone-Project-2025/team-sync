package ru.teamsync.projects.integration.utils;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.teamsync.projects.config.properties.JwtProperties;

import java.util.Date;

@Service
public class JwtUtilityService {

    private static final String INTERNAL_ID_CLAIM = "internal_id";

    @Autowired
    private JwtProperties jwtProperties;

    public String generateTokenWithUserId(int userId) {
        return Jwts.builder()
                .claim(INTERNAL_ID_CLAIM, userId)
                .issuer(jwtProperties.issuer())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.expirationTimeMs()))
                .signWith(jwtProperties.getSecurityKey())
                .compact();
    }

}
