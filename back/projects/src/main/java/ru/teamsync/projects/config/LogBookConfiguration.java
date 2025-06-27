package ru.teamsync.projects.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.zalando.logbook.Logbook;
import org.zalando.logbook.DefaultSink;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.DefaultHttpLogWriter;

@Configuration
public class LogBookConfiguration {

    @Bean
    public Logbook logbook() {
        return Logbook.builder()
                .sink(new DefaultSink(
                    new JsonHttpLogFormatter(),
                    new DefaultHttpLogWriter()
                ))
                .build();
    }
}