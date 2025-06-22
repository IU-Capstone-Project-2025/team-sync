package ru.teamsync.auth.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.teamsync.auth.services.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class InternalJwtFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Value("${teamsync.security.skip-internal-jwt-filter-path-prefixes}")
    private final List<String> skipFilterPrefixes;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        authenticate(request);
        filterChain.doFilter(request, response);
    }

    private void authenticate(HttpServletRequest request) {
        String authHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeaderValue == null) {
            return;
        }

        String jwt = authHeaderValue.substring(AUTH_HEADER_PREFIX.length());
        String email = jwtService.extractEmail(jwt);

        if (email != null && !email.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (userDetails != null && jwtService.isTokenValid(jwt)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return skipFilterPrefixes.stream()
                .anyMatch(prefix -> request.getRequestURI().startsWith(prefix));
    }

}
