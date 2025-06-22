package ru.teamsync.auth.services.login;

public class UserIsNotRegisteredException extends RuntimeException {

    public UserIsNotRegisteredException(String message) {
        super(message);
    }

    public static UserIsNotRegisteredException withEmail(String email) {
        return new UserIsNotRegisteredException("User with email " + email + " was not registered");
    }

}
