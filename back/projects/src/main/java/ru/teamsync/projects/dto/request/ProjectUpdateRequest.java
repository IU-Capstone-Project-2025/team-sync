package ru.teamsync.projects.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class ProjectUpdateRequest {
    private String courseName;
    private Long teamLeadId;
    private String description;
    private String projectLink;
    private String status;
    private List<Long> skills;
    private List<Long> roles;
}
