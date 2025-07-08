package ru.teamsync.auth.client.dto.student;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StudentCreationResponse(
        @JsonProperty("student_id")
        Integer studentId,

        @JsonProperty("person_id")
        Integer personId
) {
}
