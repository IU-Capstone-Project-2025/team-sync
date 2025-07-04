package ru.teamsync.projects.service.exception;

public class ApplicationNotFoundException extends NotFoundException {

    public ApplicationNotFoundException(String message) {
        super(message);
    }

    public static ApplicationNotFoundException withId(Long applicationId) {
        return new ApplicationNotFoundException("Application with id " + applicationId + " not foud");
    }

}
