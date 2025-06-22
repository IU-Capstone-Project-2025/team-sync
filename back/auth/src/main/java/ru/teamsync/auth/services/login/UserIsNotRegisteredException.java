package ru.teamsync.auth.services.login;

import ru.teamsync.auth.services.AuthConflictException;

public class UserIsNotRegisteredException extends AuthConflictException {

    public UserIsNotRegisteredException(String message) {
        super(message);
    }

    public static UserIsNotRegisteredException withEmail(String email) {
        return new UserIsNotRegisteredException("User with email " + email + " was not registered");
    }

}
