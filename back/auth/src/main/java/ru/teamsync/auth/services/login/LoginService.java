package ru.teamsync.auth.services.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.teamsync.auth.model.SecurityUserRepository;
import ru.teamsync.auth.services.JwtService;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final SecurityUserRepository securityUserRepository;
    private final JwtService jwtService;
    
    public String generateJwtByEmail(String email) {
        if (!securityUserRepository.existsByEmail(email)) {
            throw UserIsNotRegisteredException.withEmail(email);
        }
        return jwtService.generateTokenWithEmail(email);
    }

}
