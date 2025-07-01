package ru.teamsync.resume.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j2;
import ru.teamsync.resume.dto.response.BaseResponse;

import org.springframework.web.servlet.resource.NoResourceFoundException;


@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleServiceNotFound(NotFoundException ex) {
        log.error("NotFoundException: ", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.withErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleNotFound(NoResourceFoundException ex) {
        log.error(ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.withErrorMessage(ex.getResourcePath()));
    }

    @ExceptionHandler(ResourceAccessDeniedException.class)
    public ResponseEntity<BaseResponse<Void>> handleAccessDenied(ResourceAccessDeniedException ex) {
        log.error("ResourceAccessDeniedException: ", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(BaseResponse.withErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<BaseResponse<Void>> handleServiceException(ServiceException ex) {
        log.error("ServiceException: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.withErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleAny(Exception ex) {
        log.error(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.withErrorMessage(ex.getMessage()));
    }

}
