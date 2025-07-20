package ru.teamsync.projects.service.projects;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.teamsync.grpc.ProjectsRecommendationsGrpc;
import ru.teamsync.grpc.RecommendationProto;
import ru.teamsync.projects.dto.response.ProjectResponse;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.mapper.ProjectMapper;
import ru.teamsync.projects.repository.ProjectRepository;
import ru.teamsync.projects.service.projects.utils.ProjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectRecommendationsService {

    @GrpcClient("project-recommendations-service")
    private ProjectsRecommendationsGrpc.ProjectsRecommendationsBlockingStub projectsRecommendationsStub;

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectUtils projectUtils;


    public List<ProjectResponse> getProjectRecommendationsForStudent(
            long studentId,
            FiltrationParameters filterParams,
            Pageable pageable
    ) {
        List<Long> projectIds = fetchProjectIdsForStudent(studentId, pageable);
        Specification<Project> specs = projectUtils.buildSpecification(filterParams);

        List<Project> projects = findAllProjectsInOrder(projectIds, specs);
        return projects.stream()
                .map(projectMapper::toDto)
                .map(projectUtils::enrichWithMemberCount)
                .toList();
    }

    private List<Long> fetchProjectIdsForStudent(long studentId, Pageable pageable) {
        int readStart = pageable.getPageSize() * pageable.getPageNumber();
        int amount = pageable.getPageSize();
        var request = RecommendationProto.ProjectsRecommendationsRequest
                .newBuilder()
                .setStudentId((int) studentId)
                .setReadStart(readStart)
                .setAmount(amount)
                .build();

        return projectsRecommendationsStub
                .fetchRecommendation(request)
                .getProjectScoresList()
                .stream()
                .map(RecommendationProto.ProjectScore::getProjectId)
                .map(Integer::longValue)
                .toList();
    }

    private List<Project> findAllProjectsInOrder(List<Long> ids, Specification<Project> specs) {
        Specification<Project> combinedSpec = specs.and((root, query, cb) -> root.get("id").in(ids));

        List<Project> filteredProjects = projectRepository.findAll(combinedSpec);

        Map<Long, Project> projectById = filteredProjects.stream()
                .collect(Collectors.toMap(Project::getId, Function.identity()));

        return ids.stream()
                .map(projectById::get)
                .filter(Objects::nonNull)
                .toList();
    }
}
