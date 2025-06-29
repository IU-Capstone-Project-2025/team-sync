package ru.teamsync.resume.config.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpLogWriter;
import org.zalando.logbook.Precorrelation;

public class LogbackHttpLogWriter implements HttpLogWriter {
    private static final Logger log = LoggerFactory.getLogger("org.zalango.logbook");

    @Override
    public void write(Precorrelation precorrelation, String request) {
        log.info(request);
    }

    @Override
    public void write(Correlation correlation, String response) {
        log.info(response);
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
