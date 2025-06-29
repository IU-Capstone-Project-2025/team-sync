package ru.teamsync.resume.dto;

public class StudyGroupNotFoundException extends RuntimeException {
    public StudyGroupNotFoundException(String studyGroupName) {
        super("Study group not found: " + studyGroupName);
    }
}
