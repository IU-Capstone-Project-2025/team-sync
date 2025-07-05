package ru.teamsync.projects.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ProjectUpdateRequest(
    @JsonProperty("name")
    String name,

    @JsonProperty("course_id")
    String courseId,

    @JsonProperty("description")
    String description,

    @JsonProperty("project_link")
    String projectLink,

    @JsonProperty("status")
    String status,

    @JsonProperty("skills")
    List<Long> skills,

    @JsonProperty("roles")
    List<Long> roles
) {}