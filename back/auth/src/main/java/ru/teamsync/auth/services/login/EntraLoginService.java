package ru.teamsync.auth.services.login;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import ru.teamsync.auth.model.SecurityUser;
import ru.teamsync.auth.model.SecurityUserRepository;
import ru.teamsync.auth.services.jwt.JwtService;
import ru.teamsync.auth.services.jwt.JwtUserClaims;
import ru.teamsync.auth.services.jwt.JwtUserClaimsMapper;

@Service
@RequiredArgsConstructor
public class EntraLoginService implements LoginService {

    private final static String EMAIL_CLAIM = "preferred_username";

    private final SecurityUserRepository securityUserRepository;
    private final JwtService jwtService;
    private final JwtUserClaimsMapper claimsMapper;

    @Override
    public String generateInternalJwt(Jwt externalJwt) {
        String email = externalJwt.getClaimAsString(EMAIL_CLAIM);

        SecurityUser securityUser = securityUserRepository
                .findByEmail(email)
                .orElseThrow(() -> UserIsNotRegisteredException.withEmail(email));

        var claims = claimsMapper.toClaims(securityUser);

        return jwtService.generateToken(claims);
    }

}
