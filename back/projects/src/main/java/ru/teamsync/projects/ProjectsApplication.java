package ru.teamsync.projects;

import io.github.danielliu1123.httpexchange.EnableExchangeClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import ru.teamsync.projects.config.properties.JwtProperties;
import ru.teamsync.projects.config.properties.SecurityWebProperties;

@SpringBootApplication
@EnableExchangeClients(basePackages = "ru.teamsync.projects.client")
@EnableConfigurationProperties({
        JwtProperties.class,
        SecurityWebProperties.class
})
public class ProjectsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectsApplication.class, args);
    }

}