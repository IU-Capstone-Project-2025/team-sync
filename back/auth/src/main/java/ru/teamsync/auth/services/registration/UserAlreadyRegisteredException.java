package ru.teamsync.auth.services.registration;

import ru.teamsync.auth.services.AuthConflictException;

public class UserAlreadyRegisteredException extends AuthConflictException {

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }

    public static UserAlreadyRegisteredException withEmail(String email) {
        return new UserAlreadyRegisteredException("User with email " + email + " already registered");
    }

}
