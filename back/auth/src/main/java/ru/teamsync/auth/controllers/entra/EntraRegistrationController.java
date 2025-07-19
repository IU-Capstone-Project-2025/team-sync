package ru.teamsync.auth.controllers.entra;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.teamsync.auth.controllers.request.RegisterProfessorRequest;
import ru.teamsync.auth.controllers.request.RegisterStudentRequest;
import ru.teamsync.auth.controllers.response.AccessTokenResponse;
import ru.teamsync.auth.controllers.response.BaseResponse;
import ru.teamsync.auth.services.registration.EntraRegistrationService;

@RestController
@RequestMapping("entra/registration")
@RequiredArgsConstructor
@Tag(name = "Entra Registration", description = "Register students and professors using Entra ID")
public class EntraRegistrationController {

    private final EntraRegistrationService entraRegistrationService;

    @Operation(
        summary = "Register a student",
        description = "Registers a new student and returns an internal JWT"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Student successfully registered"
    )
    @PostMapping("/student")
    public BaseResponse<AccessTokenResponse> registerStudent(@AuthenticationPrincipal Jwt entraJwt, @RequestBody RegisterStudentRequest registerStudentRequest) {
        String jwt = entraRegistrationService.registerStudentAndGetJwt(entraJwt, registerStudentRequest);
        return BaseResponse.of(new AccessTokenResponse(jwt));
    }

    @Operation(
        summary = "Register a professor",
        description = "Registers a new professor and returns an internal JWT"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Professor successfully registered"
    )
    @PostMapping("/professor")
    public BaseResponse<AccessTokenResponse> registerProfessor(@AuthenticationPrincipal Jwt entraJwt, @RequestBody RegisterProfessorRequest registerProfessorRequest) {
        String jwt = entraRegistrationService.registerProfessorAndGetJwt(entraJwt, registerProfessorRequest);
        return BaseResponse.of(new AccessTokenResponse(jwt));
    }

}
