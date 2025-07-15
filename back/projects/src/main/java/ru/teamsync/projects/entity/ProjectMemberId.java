package ru.teamsync.projects.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class ProjectMemberId implements Serializable {

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;
}