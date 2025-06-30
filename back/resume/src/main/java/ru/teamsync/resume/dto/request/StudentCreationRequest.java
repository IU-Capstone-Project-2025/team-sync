package ru.teamsync.resume.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;

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
    
    @JsonProperty("study_group")
    private String studyGroup;

    private String description;
    @JsonProperty("github_alias")
    private String githubAlias;

    @JsonProperty("tg_alias")
    private String tgAlias;

}
