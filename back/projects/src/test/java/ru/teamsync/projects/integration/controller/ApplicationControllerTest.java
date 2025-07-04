package ru.teamsync.projects.integration.controller;


import org.junit.jupiter.api.Test;
import ru.teamsync.projects.integration.IntegrationEnvironment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ApplicationControllerTest extends IntegrationEnvironment {


    @Test
    public void should_returnApplications_when_requestingByProject() throws Exception {
        //Arrange
        int courseId = jdbcClient
            .sql("INSERT INTO course(name) VALUES ('Test Course') RETURNING id")
            .query(Integer.class)
            .single();
        int userId = jdbcClient
                .sql("INSERT INTO person(name, surname, email) VALUES ('test', 'test', 'testmail') RETURNING id")
                .query(Integer.class).single();
        int applicantPersonId = jdbcClient
                .sql("INSERT INTO person(name, surname, email) VALUES ('applicant', 'test', 'applicatntemail') RETURNING id")
                .query(Integer.class).single();
        int applicantId = studentUtilityService.createStudentWithPersonId(applicantPersonId);

        int projectId = jdbcClient.sql(
                        "INSERT INTO project(name, course_id, team_lead_id, description, project_link, status) VALUES ('a', :courseId, :userId, 'a', 'a', 'ACTIVE') RETURNING id"
                )
                .param("courseId", courseId)
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

        //Act
        mockMvc.perform(
                        get("/applications/project/{projectId}", projectId)
                                .header("Authorization", "Bearer " + jwt)
                )
                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(1));
    }
}
