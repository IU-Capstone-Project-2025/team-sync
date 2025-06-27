package ru.teamsync.recommendation.service.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.teamsync.grpc.ProjectsRecommendationsGrpc;
import ru.teamsync.grpc.RecommendationProto;
import ru.teamsync.grpc.RecommendationProto.ProjectsRecommendationsRequest;
import ru.teamsync.grpc.RecommendationProto.ProjectsRecommendationsResponse;
import ru.teamsync.recommendation.service.UserProjectService;

@GrpcService
@Log4j2
@RequiredArgsConstructor
public class ProjectsRecommendationServiceGrpcImpl extends ProjectsRecommendationsGrpc.ProjectsRecommendationsImplBase {

    public final UserProjectService userProjectService;

    @Override
    public void fetchRecommendation(ProjectsRecommendationsRequest request, StreamObserver<ProjectsRecommendationsResponse> responseObserver) {
        log.info("Got fetchRecommendation request: {}", request);

        ProjectsRecommendationsResponse.Builder responseBuilder = ProjectsRecommendationsResponse.newBuilder();

        var projectScores = userProjectService.getProjectScores(request.getUserId(), request.getReadStart(), request.getReadStart() + request.getAmount());
        projectScores.stream().map(projectScore -> {
            return RecommendationProto.ProjectScore.newBuilder()
                    .setProjectId(projectScore.getProjectId())
                    .setScore(projectScore.getScore())
                    .build();
        }).forEach(responseBuilder::addProjectScores);

        log.info("Returning {} objects", projectScores.size());
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
