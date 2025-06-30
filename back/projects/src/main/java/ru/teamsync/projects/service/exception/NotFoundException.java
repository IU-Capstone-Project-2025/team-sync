package ru.teamsync.projects.service.exception;

import org.hibernate.annotations.NotFound;

public class NotFoundException extends ServiceException {

    public NotFoundException(String message) {
        super(message);
    }

}
