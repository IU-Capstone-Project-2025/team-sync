package ru.teamsync.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.teamsync.auth.client.ResumeClientException;
import ru.teamsync.auth.controllers.response.BaseResponse;
import ru.teamsync.auth.services.AuthConflictException;

@RestControllerAdvice
public class BaseController {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleNotFound(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.withErrorMessage(ex.getResourcePath()));
    }

    @ExceptionHandler(AuthConflictException.class)
    public ResponseEntity<BaseResponse<Void>> handleUserNotRegistered(AuthConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(BaseResponse.withErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(ResumeClientException.class)
    public ResponseEntity<BaseResponse<Void>> handleUserNotRegistered(ResumeClientException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.withErrorMessage(ex.getMessage()));
    }
}
