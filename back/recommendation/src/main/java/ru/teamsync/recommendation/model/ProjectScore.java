package ru.teamsync.recommendation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ProjectScore {
    private int projectId;
    private float score;
}
