package ru.teamsync.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.teamsync.auth.controllers.request.RegisterProfessorRequest;
import ru.teamsync.auth.controllers.request.RegisterStudentRequest;
import ru.teamsync.auth.controllers.respons.AccessTokenResponse;
import ru.teamsync.auth.controllers.respons.BaseResponse;
import ru.teamsync.auth.services.RegistrationService;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/student")
    public BaseResponse<AccessTokenResponse> registerStudent(@AuthenticationPrincipal Jwt entraJwt, @RequestBody RegisterStudentRequest registerStudentRequest) {
        String jwt = registrationService.registerStudentAndGetJwt(entraJwt, registerStudentRequest);
        return BaseResponse.of(new AccessTokenResponse(jwt));
    }

    @PostMapping("/professor")
    public BaseResponse<AccessTokenResponse> registerProfessor(@AuthenticationPrincipal Jwt entraJwt, @RequestBody RegisterProfessorRequest registerProfessorRequest) {
        String jwt = registrationService.registerProfessorAndGetJwt(entraJwt, registerProfessorRequest);
        return BaseResponse.of(new AccessTokenResponse(jwt));
    }

}
