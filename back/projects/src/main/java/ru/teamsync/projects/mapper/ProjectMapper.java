package ru.teamsync.projects.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import ru.teamsync.projects.dto.request.ProjectCreateRequest;
import ru.teamsync.projects.dto.request.ProjectUpdateRequest;
import ru.teamsync.projects.dto.response.ProjectResponse;
import ru.teamsync.projects.entity.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "membersCount", ignore = true)
    ProjectResponse toDto(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "teamLeadId", source = "userId")
    @Mapping(target = "status", expression = "java(ru.teamsync.projects.entity.ProjectStatus.valueOf(request.status().toUpperCase()))")
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "projectLink", source = "request.projectLink")
    @Mapping(target = "skillIds", source = "request.skills")
    @Mapping(target = "roleIds", source = "request.roles")
    @Mapping(target = "requiredMembersCount", source = "request.requiredMembersCount")
    Project toEntity(ProjectCreateRequest request, Long userId);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "status", expression = "java(request.status() != null ? ru.teamsync.projects.entity.ProjectStatus.valueOf(request.status().toUpperCase()) : null)")
    @Mapping(source = "skills", target = "skillIds")
    @Mapping(source = "roles", target = "roleIds")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "requiredMembersCount", source = "request.requiredMembersCount")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teamLeadId", ignore = true) 
    @Mapping(target = "course", ignore = true)
    void updateEntity(ProjectUpdateRequest request, @MappingTarget Project project);
}