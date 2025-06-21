package ru.teamsync.projects.entity;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
