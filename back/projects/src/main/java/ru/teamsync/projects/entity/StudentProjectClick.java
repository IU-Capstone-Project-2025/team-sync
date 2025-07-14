package ru.teamsync.projects.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student_project_click")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class StudentProjectClick {

    @EmbeddedId
    private StudentProjectClickId id;

    @Column(name = "click_count", nullable = false)
    private Integer clickCount;
}
