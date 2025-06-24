package ru.teamsync.auth.controllers.entra;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.teamsync.auth.controllers.response.AccessTokenResponse;
import ru.teamsync.auth.controllers.response.BaseResponse;
import ru.teamsync.auth.services.login.EntraLoginService;

@RestController
@RequestMapping("/entra/login")
@RequiredArgsConstructor
public class EntraLoginController {

    private final EntraLoginService entraLoginService;

    @GetMapping
    public BaseResponse<AccessTokenResponse> login(@AuthenticationPrincipal Jwt jwt) {
        String token = entraLoginService.generateInternalJwt(jwt);
        return BaseResponse.of(new AccessTokenResponse(token));
    }

}
