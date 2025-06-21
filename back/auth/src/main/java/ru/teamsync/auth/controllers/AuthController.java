package ru.teamsync.auth.controllers;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import ru.teamsync.auth.services.JwtService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @GetMapping("/login")
    public String login() {
        return jwtService.generateToken("user@mail.com");
    }

    @GetMapping("/login/{var}")
    public boolean isCorrect(@PathVariable String var) {
        return jwtService.isTokenValid(var);
    }

    @GetMapping("/test")
    public String test() {
        return "Test successful!";
    }

}
