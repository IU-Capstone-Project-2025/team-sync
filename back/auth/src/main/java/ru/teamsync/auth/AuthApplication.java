package ru.teamsync.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import io.github.danielliu1123.httpexchange.EnableExchangeClients;
import ru.teamsync.auth.config.properties.InternalJwtFilterProperties;
import ru.teamsync.auth.config.properties.JwtProperties;
import ru.teamsync.auth.config.properties.SecurityWebProperties;

@SpringBootApplication
@EnableWebSecurity
@EnableMethodSecurity
@EnableExchangeClients(basePackages = "ru.teamsync.auth.client")
@EnableConfigurationProperties({
        JwtProperties.class,
        InternalJwtFilterProperties.class,
        SecurityWebProperties.class
})
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
