package ru.teamsync.projects.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface EmbedderClient {

    @PostExchange("/points/project/{projectId}")
    ResponseEntity<Void> recalculateProjectPoints(Long projectId);

}
