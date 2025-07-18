package ru.teamsync.resume.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface EmbedderClient {

    @PostExchange("/points/student/{personId}")
    ResponseEntity<Void> recalculateStudentPoints(@PathVariable Long personId);
}
