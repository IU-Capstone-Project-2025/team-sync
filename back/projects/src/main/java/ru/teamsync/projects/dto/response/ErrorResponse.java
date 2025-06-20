package ru.teamsync.projects.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) // for equals() and hashCode()
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse extends BaseResponse {
    private Object data;
}

