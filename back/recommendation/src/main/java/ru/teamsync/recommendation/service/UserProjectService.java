package ru.teamsync.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;
import ru.teamsync.recommendation.model.ProjectScore;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserProjectService {

    private final ListOperations<String, ProjectScore> listOps;

    public List<ProjectScore> getProjectScores(long studentId, long start, long end) {
        String key = String.valueOf(studentId);
        return listOps.range(key, start, end-1);
    }

}