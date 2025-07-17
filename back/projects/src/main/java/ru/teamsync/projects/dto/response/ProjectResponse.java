package ru.teamsync.projects.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.teamsync.projects.entity.ProjectStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String name;
    private Long courseId;
    private Long teamLeadId;
    private String description;
    private String projectLink;
    private ProjectStatus status;
    private List<Long> skillIds;
    private List<Long> roleIds;
    private Integer requiredMembersCount;
    private Integer membersCount;
}
