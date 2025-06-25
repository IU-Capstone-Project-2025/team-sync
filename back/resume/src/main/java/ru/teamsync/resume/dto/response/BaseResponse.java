package ru.teamsync.resume.dto.response;

public record BaseResponse<T>(
        ErrorResponse error,
        Boolean success,
        T data
) {
    public static <T> BaseResponse<T> of(T data) {
        return new BaseResponse<>(null, true, data);
    }

    public static <T> BaseResponse<T> withError(String code, String message) {
        return new BaseResponse<>(new ErrorResponse(code, message), false, null);
    }

    public static <T> BaseResponse<T> withErrorMessage(String message) {
        return withError(null, message);
    }
}