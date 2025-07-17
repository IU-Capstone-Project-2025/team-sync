package ru.teamsync.projects.dto.response;

import java.util.List;

public record PageResponse<C>(
        List<C> content
        //TODO fill with page fields
) {

    public static <C> PageResponse<C> withContent(List<C> content) {
        return new PageResponse<>(content);
    }

}
