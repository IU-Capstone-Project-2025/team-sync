package ru.teamsync.resume.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCreationRequest {

    private PersonCreationRequest person;
    private String studyGroup;
    private String description;
    private String githubAlias;
    private String tgAlias;

}
