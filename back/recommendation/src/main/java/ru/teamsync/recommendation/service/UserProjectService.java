package ru.teamsync.recommendation.service;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.teamsync.recommendation.model.ProjectScore;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProjectService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ListOperations<String, Object> listOps;

    public UserProjectService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.listOps = redisTemplate.opsForList();
    }

    public void addProjectScore(String userId, ProjectScore projectScore) {
        String key = "user:" + userId + ":projects";
        listOps.rightPush(key, projectScore);
    }

    public List<ProjectScore> getProjectScores(int userId) {
        String key = "user:" + userId + ":projects";
        List<Object> rawList = listOps.range(key, 0, -1);

        return rawList.stream()
                .map(obj -> (ProjectScore) obj)
                .collect(Collectors.toList());
    }

    public void deleteUserProjects(String userId) {
        redisTemplate.delete("user:" + userId + ":projects");
    }
}