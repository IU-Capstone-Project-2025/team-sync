package ru.teamsync.auth.client.dto.professor;


import com.fasterxml.jackson.annotation.JsonProperty;

public record ProfessorCreationResponse(
        @JsonProperty("professor_id")
        Integer professorId,

        @JsonProperty("person_id")
        Integer personId
) {
}
