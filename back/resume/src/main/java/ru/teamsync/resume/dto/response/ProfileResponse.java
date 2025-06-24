package ru.teamsync.resume.dto.response;

public record ProfileResponse(
    String type,                         // "student" | "professor"
    PersonResponse person,
    Object profile                       // StudentProfileResponse | ProfessorProfileResponse
) {}