package ru.teamsync.projects.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
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
    List<Long> skills,
    
    @JsonProperty("roles")
    List<Long> roles,

    @JsonProperty("required_members_count")
    Integer requiredMembersCount
) {}