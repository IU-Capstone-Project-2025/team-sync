package ru.teamsync.auth.services.registration;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import ru.teamsync.auth.controllers.request.RegisterProfessorRequest;
import ru.teamsync.auth.controllers.request.RegisterStudentRequest;

public interface RegistrationService {
    @Transactional
    String registerStudentAndGetJwt(Jwt entraJwt, RegisterStudentRequest registerStudentRequest);

    @Transactional
    String registerProfessorAndGetJwt(Jwt entraJwt, RegisterProfessorRequest registerProfessorRequest);
}
