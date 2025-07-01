package ru.teamsync.resume.service.exception;

public class StudyGroupNotFoundException extends NotFoundException {

    public StudyGroupNotFoundException(String message) {
        super(message);
    }

    public static StudyGroupNotFoundException withName(String studyGroupName) {
        return new StudyGroupNotFoundException("Study group with name " + studyGroupName + " not found");
    }

}
