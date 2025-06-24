package ru.teamsync.auth.config.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

public class ClientProperties {

    @ConfigurationProperties(prefix = "teamsync.client.resume")
    public record ResumeServiceClientProperties(
            String apiUrl
    ) {

    }
}
