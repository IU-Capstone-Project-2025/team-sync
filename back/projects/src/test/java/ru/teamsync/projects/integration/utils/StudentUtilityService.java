package ru.teamsync.projects.integration.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
public class StudentUtilityService {

    @Autowired
    private JdbcClient jdbcClient;

    public int createStudentWithPersonId(int personId) {
        int studyGroupId =
                jdbcClient.sql("INSERT INTO study_group(name) VALUES (:study_group_name) RETURNING id")
                        .param("study_group_name", "Test Study Group For Person ID " + personId)
                        .query(Integer.class)
                        .single();

        return jdbcClient.
                sql("INSERT INTO student (person_id, study_group_id, github_alias, tg_alias) VALUES (:person_id, :study_group_id, :github_alias, :tg_alias) RETURNING id")
                .param("person_id", personId)
                .param("study_group_id", studyGroupId)
                .param("github_alias", "test-github-alias-" + personId)
                .param("tg_alias", "test-tg-alias-" + personId)
                .query(Integer.class)
                .single();
    }

}
