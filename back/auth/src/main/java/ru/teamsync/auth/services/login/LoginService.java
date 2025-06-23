package ru.teamsync.auth.services.login;

import org.springframework.security.oauth2.jwt.Jwt;

public interface LoginService {

    String generateInternalJwt(Jwt externalJwt);

}
