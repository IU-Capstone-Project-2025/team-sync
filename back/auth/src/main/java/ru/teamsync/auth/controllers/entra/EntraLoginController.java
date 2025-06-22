package ru.teamsync.auth.controllers.entra;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.teamsync.auth.controllers.respons.AccessTokenResponse;
import ru.teamsync.auth.controllers.respons.BaseResponse;
import ru.teamsync.auth.services.login.LoginService;

@RestController
@RequestMapping("/entra/login")
@RequiredArgsConstructor
public class EntraLoginController {

    private final LoginService loginService;

    @GetMapping
    public BaseResponse<AccessTokenResponse> login(@AuthenticationPrincipal Jwt jwt) {
        String token = loginService.generateJwtByEmail(jwt.getClaim("preferred_username"));
        return BaseResponse.of(new AccessTokenResponse(token));
    }

}
