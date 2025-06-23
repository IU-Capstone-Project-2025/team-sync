package ru.teamsync.projects.dto.response;

public record BaseResponse<T>(
        T data,
        Boolean success,
        ErrorResponse error
) {

    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<>(data, true, null);
    }

    public static <T> BaseResponse<T> error(String code, String message) {
        return new BaseResponse<>(null, false, new ErrorResponse(code, message));
    }
}
