package ru.teamsync.projects.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
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
    private List<Long> skills;

    @NotEmpty
    private List<Long> roles;
}
