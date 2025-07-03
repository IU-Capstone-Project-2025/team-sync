package ru.teamsync.projects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import ru.teamsync.projects.config.properties.JwtProperties;
import ru.teamsync.projects.config.properties.PaginationDefaultProperties;
import ru.teamsync.projects.config.properties.SecurityWebProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        JwtProperties.class,
        SecurityWebProperties.class,
        PaginationDefaultProperties.class
})
public class ProjectsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectsApplication.class, args);
    }

}