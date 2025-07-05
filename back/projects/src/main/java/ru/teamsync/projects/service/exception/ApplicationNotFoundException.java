package ru.teamsync.projects.service.exception;

public class ApplicationNotFoundException extends NotFoundException {

    public ApplicationNotFoundException(String message) {
        super(message);
    }

    public static ApplicationNotFoundException withId(Long applicationtId) {
        return new ApplicationNotFoundException("Application with id " + applicationtId + " not foud");
    }
}