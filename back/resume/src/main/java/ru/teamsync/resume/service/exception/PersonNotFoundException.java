package ru.teamsync.resume.service.exception;

public class PersonNotFoundException extends NotFoundException {

    public PersonNotFoundException(String message) {
        super(message);
    }

    public static PersonNotFoundException withId(Long personId) {
        return new PersonNotFoundException("Person with id " + personId + " not found");
    }

}
