package ru.teamsync.projects.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ProjectUpdateRequest(
    @JsonProperty("name")
    String name,

    @JsonProperty("course_name")
    String courseName,

    @JsonProperty("description")
    String description,

    @JsonProperty("project_link")
    String projectLink,

    @JsonProperty("status")
    String status,

    @JsonProperty("skills")
    List<Long> skills,

    @JsonProperty("roles")
    List<Long> roles,
    
    @JsonProperty("required_members_count")
    Integer requiredMembersCount,

    @JsonProperty("members_to_remove")
    List<Long> membersToRemove
) {}