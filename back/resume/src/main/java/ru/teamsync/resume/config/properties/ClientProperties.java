package ru.teamsync.resume.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

public class ClientProperties {

    @ConfigurationProperties(prefix="teamsync.client.embedder")
    public record EmbedderClientProperties(
            String apiUrl
    ) {

    }

}
