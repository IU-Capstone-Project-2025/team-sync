package ru.teamsync.recommendation;

import io.grpc.stub.StreamObserver;
import ru.teamsync.grpc.ProjectsRecommendationsGrpc;
import ru.teamsync.grpc.RecommendationProto.ProjectsRecommendationsRequest;
import ru.teamsync.grpc.RecommendationProto.ProjectsRecommendationsResponse;

public class ProjectsRecommendationService extends ProjectsRecommendationsGrpc.ProjectsRecommendationsImplBase {
    @Override
    public void fetchRecommendation(ProjectsRecommendationsRequest request, StreamObserver<ProjectsRecommendationsResponse> responseObserver) {

    }
}
