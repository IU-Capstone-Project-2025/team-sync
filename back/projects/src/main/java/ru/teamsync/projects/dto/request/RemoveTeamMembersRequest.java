package ru.teamsync.projects.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RemoveTeamMembersRequest(
    @JsonProperty("members_to_remove")
    List<Long> membersToRemove
) {}
