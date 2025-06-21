package ru.teamsync.projects.dto.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String courseName;
    private Long teamLeadId;
    private String description;
    private String projectLink;
    private String status;

    private List<Long> skills;
    private List<Long> roles;
}
