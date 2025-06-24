package ru.teamsync.auth.client.dto.professor;

import lombok.Builder;
import lombok.Data;
import ru.teamsync.auth.client.dto.PersonCreationRequest;

@Data
@Builder
public class ProfessorCreationRequest {

    private PersonCreationRequest person;
    private String tgAlias;

}
