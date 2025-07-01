package ru.teamsync.resume.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ProfessorCreationRequest {

    private PersonCreationRequest person;

    @JsonProperty("tg_alias")
    private String tgAlias;

}
