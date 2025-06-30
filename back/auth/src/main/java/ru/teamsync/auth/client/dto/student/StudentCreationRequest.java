package ru.teamsync.auth.client.dto.student;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.teamsync.auth.client.dto.PersonCreationRequest;

@Data
@Builder
@Getter
@Setter
public class StudentCreationRequest {

    private PersonCreationRequest person;
    private String studyGroup;
    private String description;
    private String githubAlias;
    private String tgAlias;

}
