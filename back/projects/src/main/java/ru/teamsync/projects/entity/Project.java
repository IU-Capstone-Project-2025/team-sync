package ru.teamsync.projects.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String courseName;
    private Long teamLeadId;
    private String description;
    private String projectLink;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @ElementCollection
    @CollectionTable(
            name = "project_skill",
            joinColumns = @JoinColumn(name = "project_id")
    )
    @Column(name = "skill_id")
    private List<Long> skillIds;

    @ElementCollection
    @CollectionTable(
            name = "project_role",
            joinColumns = @JoinColumn(name = "project_id")
    )
    @Column(name = "role_id")
    private List<Long> roleIds;
}
