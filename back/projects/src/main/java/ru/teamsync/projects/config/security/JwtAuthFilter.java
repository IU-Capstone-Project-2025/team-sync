package ru.teamsync.projects.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.teamsync.projects.service.security.JwtService;
import ru.teamsync.projects.service.security.SecurityUser;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeaderValue != null && authHeaderValue.startsWith(AUTH_HEADER_PREFIX)) {

            String jwt = authHeaderValue.substring(AUTH_HEADER_PREFIX.length());

            if (jwtService.isTokenValid(jwt)) {
                SecurityUser securityUser = jwtService.extractSecurityUser(jwt);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        securityUser, null, List.of()
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
