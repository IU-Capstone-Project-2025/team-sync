package ru.teamsync.projects.service.exception;

public class CannotApplyToOwnProjectException extends ServiceException {

    public CannotApplyToOwnProjectException(String message) {
        super(message);
    }

    public static CannotApplyToOwnProjectException forProject(Long projectId) {
        return new CannotApplyToOwnProjectException("\"You cannot apply to your own project with id \" + projectId");
    }

}
