package ru.teamsync.recommendation.service.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.teamsync.grpc.ProjectsRecommendationsGrpc;
import ru.teamsync.grpc.RecommendationProto;
import ru.teamsync.grpc.RecommendationProto.ProjectsRecommendationsRequest;
import ru.teamsync.grpc.RecommendationProto.ProjectsRecommendationsResponse;
import ru.teamsync.recommendation.service.UserProjectService;

@Service
@RequiredArgsConstructor
public class ProjectsRecommendationServiceGrpcImpl extends ProjectsRecommendationsGrpc.ProjectsRecommendationsImplBase {

    public final UserProjectService userProjectService;

    @Override
    public void fetchRecommendation(ProjectsRecommendationsRequest request, StreamObserver<ProjectsRecommendationsResponse> responseObserver) {
        int userId = request.getUserId();
        ProjectsRecommendationsResponse.Builder responseBuilder = ProjectsRecommendationsResponse.newBuilder();

        var projectScores = userProjectService.getProjectScores(userId);
        projectScores.stream().map(projectScore -> {
            return RecommendationProto.ProjectScore.newBuilder()
                    .setProjectId(projectScore.getProjectId())
                    .setScore(projectScore.getScore())
                    .build();
        }).forEach(responseBuilder::addProjectScores);

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
