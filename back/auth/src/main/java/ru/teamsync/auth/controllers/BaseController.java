package ru.teamsync.auth.controllers;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.teamsync.auth.controllers.respons.BaseResponse;
import ru.teamsync.auth.services.login.UserIsNotRegisteredException;

@RestControllerAdvice
public class BaseController {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleNotFound(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.withErrorMessage(ex.getResourcePath()));
    }

    @ExceptionHandler(UserIsNotRegisteredException.class)
    public ResponseEntity<BaseResponse<Void>> handleUserNotRegistered(UserIsNotRegisteredException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(BaseResponse.withErrorMessage(ex.getMessage()));
    }
}
