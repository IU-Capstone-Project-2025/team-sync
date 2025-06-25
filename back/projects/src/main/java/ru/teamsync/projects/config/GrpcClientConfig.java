package ru.teamsync.projects.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.teamsync.grpc.ProjectsRecommendationsGrpc;
import ru.teamsync.projects.config.properties.RecommendationServiceProperties;

@Configuration
public class GrpcClientConfig {

    @Bean
    public ManagedChannel recommendationsManagerChannel(RecommendationServiceProperties properties) {
        return ManagedChannelBuilder.forAddress(properties.host(), properties.port())
                .usePlaintext()
                .build();
    }

    @Bean
    public ProjectsRecommendationsGrpc.ProjectsRecommendationsBlockingStub projectsRecommendationsStub(
            @Qualifier("recommendationsManagerChannel") ManagedChannel channel
    ) {
        return ProjectsRecommendationsGrpc.newBlockingStub(channel);
    }

}
