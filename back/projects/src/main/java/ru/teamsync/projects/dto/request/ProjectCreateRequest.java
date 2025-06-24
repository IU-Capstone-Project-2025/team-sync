package ru.teamsync.projects.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ProjectCreateRequest(
    @JsonProperty("course_name")
    @NotBlank
    String courseName,

    @JsonProperty("description")
    @NotBlank
    String description,

    @JsonProperty("project_link")
    String projectLink,

    @JsonProperty("status")
    @NotBlank
    String status,

    @JsonProperty("skills")
    @NotEmpty
    List<Long> skills,

    @JsonProperty("roles")
    @NotEmpty
    List<Long> roles
) {}
