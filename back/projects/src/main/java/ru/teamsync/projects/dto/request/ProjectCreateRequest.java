package ru.teamsync.projects.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record ProjectCreateRequest(
    @JsonProperty("name")
    @NotBlank
    String name,

    @JsonProperty("course_id") 
    @NotNull 
    Long courseId,

    @JsonProperty("description")
    @NotBlank
    String description,

    @JsonProperty("project_link")
    String projectLink,

    @JsonProperty("status")
    @NotBlank
    String status,

    @JsonProperty("skills")
    List<Long> skills,

    @JsonProperty("roles")
    List<Long> roles
) {}