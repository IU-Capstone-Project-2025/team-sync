package ru.teamsync.projects.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Skill {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;
}
