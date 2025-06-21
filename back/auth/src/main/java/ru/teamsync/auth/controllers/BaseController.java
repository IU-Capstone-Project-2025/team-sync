package ru.teamsync.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.withErrorMessage(e.getMessage()));
    }

//    @ExceptionHandler(UserIsNotRegisteredException.class)
//    public ResponseEntity<BaseResponse<Void>> handleUserIsNotRegisteredException(UserIsNotRegisteredException e) {
//        return ResponseEntity
//                .status(HttpStatus.CONFLICT)
//                .body(BaseResponse.withErrorMessage(e.getMessage()));
//    }
}
