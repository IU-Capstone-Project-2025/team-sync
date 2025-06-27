package ru.teamsync.projects.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.teamsync.projects.dto.response.BaseResponse;

@RestControllerAdvice
@Log4j2
public class BaseController {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleNotFound(NoResourceFoundException ex) {
        log.info(ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.withErrorMessage(ex.getResourcePath()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleAny(Exception ex) {
        log.error(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.withErrorMessage(ex.getMessage()));
    }

}
