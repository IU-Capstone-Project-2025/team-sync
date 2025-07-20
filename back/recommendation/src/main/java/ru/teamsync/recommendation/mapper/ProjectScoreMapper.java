package ru.teamsync.recommendation.mapper;

import org.mapstruct.Mapper;
import ru.teamsync.grpc.RecommendationProto;
import ru.teamsync.recommendation.config.MapstructConfig;
import ru.teamsync.recommendation.model.ProjectScore;

@Mapper(config = MapstructConfig.class)
public interface ProjectScoreMapper {

    RecommendationProto.ProjectScore toProto(ProjectScore projectScore);

}
