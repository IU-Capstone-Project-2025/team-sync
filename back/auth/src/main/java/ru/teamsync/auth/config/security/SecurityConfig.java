package ru.teamsync.auth.config.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

@Configuration
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http,
//                                           AuthFilter jwtAuthFilter,
//                                           CustomAuthManager customAuthManager) throws Exception {
//
//        return http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                        .requestMatchers("/login/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(compositeDecoder())))
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .authenticationManager(customAuthManager)
//                .build();
//    }
//
//    @Bean
//    public JwtDecoder compositeDecoder() {
//
//        String base64Secret = "c2VjcmV0c2VjcmV0c2VjcmV0c2VjcmV0c2VjcmV0c2VjcmV0";
//        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
//        NimbusJwtDecoder localDecoder = NimbusJwtDecoder.withSecretKey(key).build();
//
//        return token -> {
//            try {
//                return microsoftDecoder.decode(token);
//            } catch (JwtException e) {
//                // Not a Microsoft token, try local
//                return localDecoder.decode(token);
//            }
//        };
//    }

    // 🔐 Chain 1 — для Microsoft токенов
    @Bean
    @Order(1)
    public SecurityFilterChain microsoftSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/entra/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthFilter jwtAuthFilter,
                                           CustomAuthManager customAuthManager) throws Exception {

        return http
                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(customAuthManager)
                .build();
    }

//    // 🔐 Chain 2 — для своих JWT токенов
//    @Bean
//    @Order(2)
//    public SecurityFilterChain localJwtSecurityFilterChain(HttpSecurity http) throws Exception {
//        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(localSecret));
//
//        http
//                .securityMatcher("/api/**")
//                .authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> jwt
//                                .decoder(NimbusJwtDecoder.withSecretKey(key).build())
//                        )
//                );
//
//        return http.build();
//    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOrigins("")
                        .allowCredentials(true)
                        .allowedHeaders("*")
                        .allowedMethods("*");
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173/"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
