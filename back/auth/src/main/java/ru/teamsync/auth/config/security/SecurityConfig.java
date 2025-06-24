package ru.teamsync.auth.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.teamsync.auth.config.properties.SecurityWebProperties;

@Configuration
public class SecurityConfig {

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Bean
    @Order(1)
    public SecurityFilterChain microsoftSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/entra/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain internalSecurityFilterChain(HttpSecurity http,
                                                           InternalJwtFilter jwtInternalJwtFilter) throws Exception {

        System.out.println("Context Path: " + contextPath);
        return http
                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtInternalJwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(SecurityWebProperties securityWebProperties) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOrigins(securityWebProperties.corsAllowedOrigins())
                        .allowCredentials(true)
                        .allowedHeaders("*")
                        .allowedMethods("*");
            }
        };
    }
}
