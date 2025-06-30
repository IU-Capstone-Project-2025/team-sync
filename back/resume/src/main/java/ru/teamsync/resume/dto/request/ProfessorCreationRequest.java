package ru.teamsync.resume.dto.request;

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
    private String tgAlias;

}
