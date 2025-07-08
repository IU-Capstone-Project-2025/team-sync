package ru.teamsync.projects.integration.utils;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
public class ProjectsUtilityService {

    @Autowired
    private JdbcClient jdbcClient;

    public int saveProjectWithNameAndTeamLeadId(String name, Integer teamLeadId) {
        String courseName = "Test Course " + UUID.randomUUID();
        int courseId = jdbcClient.sql("INSERT INTO course(name) VALUES (:name) RETURNING id")
            .param("name", courseName)
            .query(Integer.class)
            .single();

        return jdbcClient.sql("INSERT INTO project(name, description, course_id, team_lead_id, project_link, status) " +
                    "VALUES (:name, :description, :course_id, :team_lead_id, :project_link, :status) RETURNING id")
                .param("name", name)
                .param("description", "Description for " + name)
                .param("course_id", courseId)
                .param("team_lead_id", teamLeadId)
                .param("project_link", "https://example.com/project/" + name)
                .param("status", "DRAFT")
                .query(Integer.class)
                .single();
    }

}
