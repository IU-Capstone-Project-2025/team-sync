package ru.teamsync.projects.integration.controller;


import org.junit.jupiter.api.Test;
import ru.teamsync.projects.integration.IntegrationEnvironment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ApplicationControllerTest extends IntegrationEnvironment {


    @Test
    public void should_returnApplications_when_requestingByProject() throws Exception {
        int userId = jdbcClient
                .sql("INSERT INTO person(name, surname, email) VALUES ('test', 'test', 'testmail') RETURNING id")
                .query(Integer.class).single();
        int applicantPersonId = jdbcClient
                .sql("INSERT INTO person(name, surname, email) VALUES ('applicant', 'test', 'applicatntemail') RETURNING id")
                .query(Integer.class).single();
        int applicantId = personUtilityService.saveStudentWithPersonId(applicantPersonId);

        int projectId = jdbcClient.sql(
                        "INSERT INTO project(name, course_name, team_lead_id, description, project_link, status) VALUES ('a', 'a', :userId, 'a', 'a', 'ACTIVE') RETURNING id"
                )
                .param("userId", userId)
                .query(Integer.class)
                .single();
        jdbcClient.sql(
                        "INSERT INTO application(project_id, student_id, status) VALUES (:projectId, :applicantId, 'PENDING')"
                )
                .param("projectId", projectId)
                .param("applicantId", applicantId)
                .update();

        String jwt = jwtUtilityService.generateTokenWithUserId(userId);

        mockMvc.perform(
                        get("/applications/project/{projectId}", projectId)
                                .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(1));
    }
}
