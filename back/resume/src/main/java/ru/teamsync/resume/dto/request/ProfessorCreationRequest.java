package ru.teamsync.resume.dto.request;

import lombok.Builder;
import lombok.Data;

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
public class ProfessorCreationRequest {

    private PersonCreationRequest person;
    private String tgAlias;

}
