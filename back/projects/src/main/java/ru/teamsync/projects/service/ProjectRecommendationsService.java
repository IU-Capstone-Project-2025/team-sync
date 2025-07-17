package ru.teamsync.projects.service;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.teamsync.grpc.ProjectsRecommendationsGrpc;
import ru.teamsync.grpc.RecommendationProto;
import ru.teamsync.projects.dto.response.ProjectResponse;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.mapper.ProjectMapper;
import ru.teamsync.projects.repository.ProjectRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectRecommendationsService {

    @GrpcClient("project-recommendations-service")
    private ProjectsRecommendationsGrpc.ProjectsRecommendationsBlockingStub projectsRecommendationsStub;

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public List<ProjectResponse> getProjectRecommendationsForUser(long userId, Pageable pageable) {
        int readStart = pageable.getPageSize() * pageable.getPageNumber();
        int amount = pageable.getPageSize();
        var request = RecommendationProto.ProjectsRecommendationsRequest
                .newBuilder()
                .setUserId(userId)
                .setReadStart(readStart)
                .setAmount(amount)
                .build();

        var projectIds = projectsRecommendationsStub
                .fetchRecommendation(request)
                .getProjectScoresList()
                .stream()
                .map(RecommendationProto.ProjectScore::getProjectId)
                .map(Integer::longValue)
                .toList();

        var projects = findAllProjectsInOrder(projectIds);

        return projects.stream().map(projectMapper::toDto).toList();
    }

    private List<Project> findAllProjectsInOrder(List<Long> ids) {
        Map<Long, Project> projectById = projectRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(Project::getId, Function.identity()));

        return ids.stream().map(projectById::get).toList();
    }
}
