package ru.teamsync.projects.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BaseResponse<T>(
        @JsonProperty("data") T data,
        @JsonProperty("success") boolean success,
        @JsonProperty("error") ErrorResponse error
) {

    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<>(data, true, null);
    }

    public static <T> BaseResponse<T> error(String code, String message) {
        return new BaseResponse<>(null, false, new ErrorResponse(code, message));
    }
}
