package ru.teamsync.resume.dto.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentCreationRequest {

    private PersonCreationRequest person;
    private String studyGroup;
    private String description;
    private String githubAlias;
    private String tgAlias;

}
