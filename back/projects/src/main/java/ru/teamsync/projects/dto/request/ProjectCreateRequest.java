package ru.teamsync.projects.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCreateRequest {
    @NotBlank
    private String courseName;

    @NotNull
    private Long teamLeadId;

    @NotBlank
    private String description;

    private String projectLink;

    @NotBlank
    private String status;

    @NotEmpty
    private List<String> skills;

    @NotEmpty
    private List<String> roles;
}
