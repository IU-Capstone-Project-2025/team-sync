package ru.teamsync.recommendation.service.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.teamsync.grpc.ProjectsRecommendationsGrpc;
import ru.teamsync.grpc.RecommendationProto.ProjectsRecommendationsRequest;
import ru.teamsync.grpc.RecommendationProto.ProjectsRecommendationsResponse;
import ru.teamsync.recommendation.mapper.ProjectScoreMapper;
import ru.teamsync.recommendation.service.UserProjectService;

@GrpcService
@Log4j2
@RequiredArgsConstructor
public class ProjectsRecommendationServiceGrpcImpl extends ProjectsRecommendationsGrpc.ProjectsRecommendationsImplBase {

    private final UserProjectService userProjectService;
    private final ProjectScoreMapper projectScoreMapper;

    @Override
    public void fetchRecommendation(ProjectsRecommendationsRequest request, StreamObserver<ProjectsRecommendationsResponse> responseObserver) {
        log.info("Got fetchRecommendation request: {}", request);

        long userId = request.getUserId();
        long start = request.getReadStart();
        long end = start + request.getAmount();

        var projectScores = userProjectService.getProjectScores(userId, start, end);

        ProjectsRecommendationsResponse.Builder responseBuilder = ProjectsRecommendationsResponse.newBuilder();
        projectScores.stream()
                .map(projectScoreMapper::toProto)
                .forEach(responseBuilder::addProjectScores);

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
