package ru.teamsync.resume.service.exception;


public class ResourceAccessDeniedException extends ServiceException {

    public ResourceAccessDeniedException(String message) {
        super(message);
    }

}