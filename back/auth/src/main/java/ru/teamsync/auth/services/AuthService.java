package ru.teamsync.auth.services;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String getInternalJwtFromExternal(Jwt jwt){
        return null;
    }

}
