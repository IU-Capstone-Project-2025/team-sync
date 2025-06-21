package ru.teamsync.projects.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private T data;
    private boolean success;
    private ErrorResponse error;

    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<>(data, true, null);
    }

    public static <T> BaseResponse<T> error(String code, String text) {
        return new BaseResponse<>(null, false, new ErrorResponse(code, text));
    }
}
