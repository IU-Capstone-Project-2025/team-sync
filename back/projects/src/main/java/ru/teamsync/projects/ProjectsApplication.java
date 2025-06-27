package ru.teamsync.projects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import ru.teamsync.projects.config.properties.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        JwtProperties.class
})
public class ProjectsApplication {
    private static final Logger log = LoggerFactory.getLogger(ProjectsApplication.class);

    public static void main(String[] args) {
        log.info("Start of backend-projects");
        SpringApplication.run(ProjectsApplication.class, args);
    }

}
