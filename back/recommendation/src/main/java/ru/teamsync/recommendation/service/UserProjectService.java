package ru.teamsync.recommendation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.teamsync.recommendation.model.ProjectScore;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserProjectService {

    private final ListOperations<String, Object> listOps;
    private final ObjectMapper objectMapper;

    public UserProjectService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.listOps = redisTemplate.opsForList();
        this.objectMapper = objectMapper;
    }

    public List<ProjectScore> getProjectScores(int userId) {
        String key = String.valueOf(userId);
        log.info("Fetching project scores for user: {}", key);
        List<Object> rawList = listOps.range(key, 0, -1);

        return rawList.stream()
                .map(obj -> objectMapper.convertValue(obj, ProjectScore.class))
                .collect(Collectors.toList());
    }

}