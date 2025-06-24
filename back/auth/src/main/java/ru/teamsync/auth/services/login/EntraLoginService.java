package ru.teamsync.auth.services.login;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import ru.teamsync.auth.model.SecurityUser;
import ru.teamsync.auth.model.SecurityUserRepository;
import ru.teamsync.auth.services.JwtService;

@Service
@RequiredArgsConstructor
public class EntraLoginService implements LoginService {

    private final static String EMAIL_CLAIM = "preferred_username";

    private final SecurityUserRepository securityUserRepository;
    private final JwtService jwtService;

    @Override
    public String generateInternalJwt(Jwt externalJwt) {
        String email = externalJwt.getClaimAsString(EMAIL_CLAIM);

        SecurityUser securityUser = securityUserRepository
                .findByEmail(email)
                .orElseThrow(() -> UserIsNotRegisteredException.withEmail(email));

        return jwtService.generateTokenWithInternalId(securityUser.getInternalUserId());
    }

}
