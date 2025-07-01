package ru.teamsync.projects.service.exception;

public class ProjectNotFoundException extends NotFoundException {

    public ProjectNotFoundException(String message) {
        super(message);
    }

    public static ProjectNotFoundException withId(Long projectId) {
        return new ProjectNotFoundException("Project with id " + projectId + " not foud");
    }

}
