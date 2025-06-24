package ru.teamsync.auth.client.dto.student;


import lombok.Builder;
import lombok.Data;
import ru.teamsync.auth.client.dto.PersonCreationRequest;

@Data
@Builder
public class StudentCreationRequest {

    private PersonCreationRequest person;
    private String studyGroup;
    private String description;
    private String githubAlias;
    private String tgAlias;

}
