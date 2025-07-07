package ru.teamsync.projects.integration.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
public class ProjectsUtilityService {

    @Autowired
    private JdbcClient jdbcClient;

    public int saveProjectWithNameAndTeamLeadId(String name, Integer teamLeadId) {
        return jdbcClient.sql("INSERT INTO project(name, description, course_name, team_lead_id, project_link, status) " +
                        "VALUES (:name, :description, :course_name, :team_lead_id, :project_link, :status) RETURNING id")
                .param("name", name)
                .param("description", "Description for " + name)
                .param("course_name", "Course for " + name)
                .param("team_lead_id", teamLeadId)
                .param("project_link", "https://example.com/project/" + name)
                .param("status", "DRAFT")
                .query(Integer.class)
                .single();
    }

}
