package ru.teamsync.resume.dto.response;

public record ProfileResponse(
        ProfileType type,                         // "student" | "professor"
        PersonResponse person,
        StudentProfileResponse studentProfile,
        ProfessorProfileResponse professorProfile  // StudentProfileResponse | ProfessorProfileResponse
) {

    public static ProfileResponse ofStudent(StudentProfileResponse studentProfile, PersonResponse person) {
        return new ProfileResponse(ProfileType.STUDENT, person, studentProfile, null);
    }

    public static ProfileResponse ofProfessor(ProfessorProfileResponse professorProfile, PersonResponse person) {
        return new ProfileResponse(ProfileType.PROFESSOR, person, null, professorProfile);
    }

}
