package ru.teamsync.recommendation.config;

import org.zalando.logbook.Logbook;
import org.zalando.logbook.core.DefaultCorrelationId;
import org.zalando.logbook.core.DefaultHttpLogFormatter;
import org.zalando.logbook.core.DefaultHttpLogWriter;
import org.zalando.logbook.core.DefaultSink;
import org.zalando.logbook.core.DefaultStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogBookConfig {

    @Bean
    public Logbook logbook() {
        return Logbook.builder()
                .sink(new DefaultSink(
                        new DefaultHttpLogFormatter(),
                        new DefaultHttpLogWriter()
                ))
                .strategy(new DefaultStrategy())
                .correlationId(new DefaultCorrelationId())
                .build();
    }
}