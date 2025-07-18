package ru.teamsync.resume.config;

import io.github.danielliu1123.httpexchange.HttpClientCustomizer;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.teamsync.resume.config.properties.ClientProperties;

@Configuration
@EnableConfigurationProperties(ClientProperties.EmbedderClientProperties.class)
@Log4j2
public class ClientConfiguration {

    @Bean
    public HttpClientCustomizer.RestClientCustomizer restClientCustomizer(ClientProperties.EmbedderClientProperties clientProperties) {
        return (client, channel) -> {
            client.baseUrl(clientProperties.apiUrl());
            client.defaultStatusHandler(
                    status -> true,
                    (request, response) -> {
                        log.error("Embedder client response code: {}", response.getStatusCode());
                    }
            );
        };
    }

}
