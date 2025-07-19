package ru.teamsync.auth.controllers.entra;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.teamsync.auth.controllers.response.AccessTokenResponse;
import ru.teamsync.auth.controllers.response.BaseResponse;
import ru.teamsync.auth.services.login.EntraLoginService;

@RestController
@RequestMapping("/entra/login")
@RequiredArgsConstructor
@Tag(name = "Entra Login", description = "Login using Entra ID and receive internal JWT")
public class EntraLoginController {

    private final EntraLoginService entraLoginService;

    @Operation(
        summary = "Login with Entra JWT",
        description = "Generates internal JWT based on the provided Entra JWT"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Internal JWT token successfully generated"
    )
    @GetMapping
    public BaseResponse<AccessTokenResponse> login(@AuthenticationPrincipal Jwt jwt) {
        String token = entraLoginService.generateInternalJwt(jwt);
        return BaseResponse.of(new AccessTokenResponse(token));
    }

}
