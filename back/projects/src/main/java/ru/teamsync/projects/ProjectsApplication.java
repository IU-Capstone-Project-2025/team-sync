package ru.teamsync.projects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import ru.teamsync.projects.config.properties.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        JwtProperties.class
})
public class ProjectsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectsApplication.class, args);
    }

}
