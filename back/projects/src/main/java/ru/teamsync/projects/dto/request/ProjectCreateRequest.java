package ru.teamsync.projects.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ProjectCreateRequest(
    @JsonProperty("name")
    @NotBlank
    String name,

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
