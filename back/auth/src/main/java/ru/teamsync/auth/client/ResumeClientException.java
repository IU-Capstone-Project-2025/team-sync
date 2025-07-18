package ru.teamsync.auth.client;


import ru.teamsync.auth.controllers.response.ErrorResponse;

public class ResumeClientException extends RuntimeException {

    public ResumeClientException(ErrorResponse errorResponse) {
        super(errorResponse.toString());
    }

    public ResumeClientException(String message) {
        super(message);
    }

}
