package ru.teamsync.projects.service.exception;

public class InvalidApplicationStatusException extends RuntimeException {
    public InvalidApplicationStatusException(String status) {
        super("Invalid application status: " + status);
    }
}