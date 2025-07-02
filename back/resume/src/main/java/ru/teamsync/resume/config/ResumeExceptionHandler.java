package ru.teamsync.resume.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.teamsync.resume.dto.StudyGroupNotFoundException;

@ControllerAdvice
public class ResumeExceptionHandler {

    @ExceptionHandler(StudyGroupNotFoundException.class)
    public ResponseEntity<String> handleStudyGroupNotFound(StudyGroupNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Study group not found: " + ex.getMessage());
    }
}