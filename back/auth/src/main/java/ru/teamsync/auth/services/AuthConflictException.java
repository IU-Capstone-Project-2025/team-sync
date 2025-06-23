package ru.teamsync.auth.services;

public class AuthConflictException extends RuntimeException {

    public AuthConflictException(String message) {
        super(message);
    }

}
