package ru.teamsync.auth.controllers;

public record BaseResponse<T>(
        Error error,
        Boolean success,
        T data
) {
    public static <T> BaseResponse<T> of(T data) {
        return new BaseResponse<>(null, true, data);
    }

    public static <T> BaseResponse<T> withError(String code, String message) {
        return new BaseResponse<>(new Error(code, message), false, null);
    }

    public static <T> BaseResponse<T> withErrorMessage(String message) {
        return withError(null, message);
    }
}

record Error(
        String code,
        String message
) {
}