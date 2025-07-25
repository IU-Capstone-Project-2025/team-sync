package ru.teamsync.resume;

import io.github.danielliu1123.httpexchange.EnableExchangeClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import ru.teamsync.resume.config.properties.JwtProperties;
import ru.teamsync.resume.config.properties.SecurityWebProperties;

@SpringBootApplication
@EnableExchangeClients(basePackages = "ru.teamsync.resume.client")
@EnableConfigurationProperties({
        JwtProperties.class,
        SecurityWebProperties.class
})
public class ResumeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeApplication.class, args);
	}

}
