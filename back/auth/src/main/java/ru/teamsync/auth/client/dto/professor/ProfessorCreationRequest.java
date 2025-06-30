package ru.teamsync.auth.client.dto.professor;

import lombok.Builder;
import lombok.Data;
import ru.teamsync.auth.client.dto.PersonCreationRequest;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class ProfessorCreationRequest {

    private PersonCreationRequest person;
    private String tgAlias;

}
