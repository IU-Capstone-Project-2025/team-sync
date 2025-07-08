package ru.teamsync.projects.dto.response;

import java.util.List;

public record PageResponse<T>(
    List<T> content,
    int totalPages,
    long totalElements,
    int number,
    int size,
    boolean first,
    boolean last
) {}
