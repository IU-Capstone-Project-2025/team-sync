package ru.teamsync.auth.controllers.entra;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.teamsync.auth.controllers.respons.AccessTokenResponse;
import ru.teamsync.auth.controllers.respons.BaseResponse;
import ru.teamsync.auth.services.JwtService;

@RestController
@RequestMapping("/entra/auth/")
@RequiredArgsConstructor
public class EntraAuthController {

    private final JwtService jwtService;

    @GetMapping("login")
    public BaseResponse<AccessTokenResponse> login(@AuthenticationPrincipal Jwt jwt) {
        String token = jwtService.generateToken(jwt.getClaim("preferred_username"));
        return BaseResponse.of(new AccessTokenResponse(token));
    }

}
