package ru.teamsync.auth.config;

import io.github.danielliu1123.httpexchange.HttpClientCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.teamsync.auth.config.properties.ClientProperties;

@Configuration
@EnableConfigurationProperties(ClientProperties.ResumeServiceClientProperties.class)
public class ClientConfiguration {

    @Bean
    public HttpClientCustomizer.RestClientCustomizer restClientCustomizer(ClientProperties.ResumeServiceClientProperties clientProperties) {
        return (client, channel) -> {
            client.baseUrl(clientProperties.apiUrl());
            client.defaultStatusHandler(
                    status -> true,
                    (request, response) -> {

                    }
            );
        };
    }

}
