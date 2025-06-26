package ru.teamsync.resume.entity;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String description;
    private String githubAlias;
    private String tgAlias;

    @Column(name = "person_id", nullable = false)
    private Long personId;

    @ElementCollection
    @CollectionTable(
        name = "student_skill",
        joinColumns = @JoinColumn(name = "student_id")
    )
    @Column(name = "skill_id")
    private List<Long> skills;

    @ElementCollection
    @CollectionTable(
        name = "student_role",
        joinColumns = @JoinColumn(name = "student_id")
    )
    @Column(name = "role_id")
    private List<Long> roles;
}
