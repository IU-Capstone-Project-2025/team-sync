package ru.teamsync.resume.service.exception;

public class ProfileUpdateAccessDeniedException extends ResourceAccessDeniedException {

    public ProfileUpdateAccessDeniedException(String message) {
        super(message);
    }

    public static ProfileUpdateAccessDeniedException withIds(Long currentUserId, Long targetPersonId) {
        return new ProfileUpdateAccessDeniedException("User with id " + currentUserId + " has no permission to update profile with id " + targetPersonId);
    }

}
