package ru.teamsync.auth.config.logging;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.HttpRequest;
import org.zalando.logbook.HttpResponse;
import org.zalando.logbook.Precorrelation;

public class CustomHttpLogFormatter implements HttpLogFormatter {

    @Override
    public String format(Precorrelation precorrelation, HttpRequest request) throws IOException {
        return "\n" + String.format("""
                             === Request ===
                             Method: %s
                             Path: %s
                             Headers:
                             %s
                             Body:
                             %s
                             ===============""",
            request.getMethod(),
            request.getPath(),
            formatHeaders(request.getHeaders()),
            indentBody(request.getBodyAsString())
        );
    }

    @Override
    public String format(Correlation correlation, HttpResponse response) throws IOException {
        return "\n" + String.format("""
                             === Response ===
                             Status: %s
                             Headers:
                             %s
                             Body:
                             %s
                             ================""",
            response.getStatus(),
            formatHeaders(response.getHeaders()),
            indentBody(response.getBodyAsString())
        );
    }

    private String formatHeaders(Map<String, List<String>> headers) {
        return headers.entrySet().stream()
            .map(entry -> String.format("  %s: %s", entry.getKey(), String.join(", ", entry.getValue())))
            .collect(Collectors.joining("\n"));
    }

    private String indentBody(String body) {
        if (body == null || body.isEmpty()) {
            return "  <empty>";
        }
        return "  " + body.replace("\n", "\n  ");
    }
}