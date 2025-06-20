package ru.teamsync.projects.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import ru.teamsync.projects.dto.model.ProjectDTO;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProjectListResponse extends BaseResponse {
    private List<ProjectDTO> data;
}
