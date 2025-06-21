package ru.teamsync.auth.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getPrincipal() == null) {
            authentication.setAuthenticated(false);
        }
        return authentication;
    }

}
