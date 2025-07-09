package ru.teamsync.projects.integration.controller;


import org.junit.jupiter.api.Test;
import ru.teamsync.projects.integration.IntegrationEnvironment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        int teamLeadId  = jdbcClient
                .sql("INSERT INTO person(name, surname, email) VALUES ('test', 'test', 'testmail') RETURNING id")
                .query(Integer.class).single();
        int applicantId = personUtilityService.savePersonWithName("Applicant");

        int projectId = jdbcClient.sql(
                        "INSERT INTO project(name, course_id, team_lead_id, description, project_link, status, required_members_count) VALUES ('a', :courseId, :teamLeadId , 'a', 'a', 'ACTIVE', 4) RETURNING id"
                )
                .param("courseId", courseId)
                .param("teamLeadId", teamLeadId)
                .query(Integer.class)
                .single();
        jdbcClient.sql(
                        "INSERT INTO application(project_id, person_id, status) VALUES (:projectId, :applicantId, 'PENDING')"
                )
                .param("projectId", projectId)
                .param("applicantId", applicantId)
                .update();

        String jwt = jwtUtilityService.generateTokenWithUserId(teamLeadId);


        mockMvc.perform(
                        get("/applications/project/{projectId}", projectId)
                                .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(1));
    }

    @Test
    public void should_returnApplications_when_requestingByPerson() throws Exception {
        int teamLeadId = personUtilityService.savePersonWithName("Test User");
        int project1Id = projectsUtilityService.saveProjectWithNameAndTeamLeadId("Test 1 Project", teamLeadId);
        int project2Id = projectsUtilityService.saveProjectWithNameAndTeamLeadId("Test 2 Project", teamLeadId);

        int applicantPersonId = personUtilityService.savePersonWithName("Applicant");

        jdbcClient.sql(
                        "INSERT INTO application(project_id, person_id, status) VALUES (:projectId, :applicantId, 'PENDING')"
                )
                .param("projectId", project1Id)
                .param("applicantId", applicantPersonId)
                .update();
        jdbcClient.sql(
                        "INSERT INTO application(project_id, person_id, status) VALUES (:projectId, :applicantId, 'PENDING')"
                )
                .param("projectId", project2Id)
                .param("applicantId", applicantPersonId)
                .update();


        String jwt = jwtUtilityService.generateTokenWithUserId(applicantPersonId);


        mockMvc.perform(
                        get("/applications/my", applicantPersonId)
                                .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(2));
    }

    @Test
    public void should_saveApplication_when_applyingToProject() throws Exception {
        int teamLeadId = personUtilityService.savePersonWithName("name");
        int projectId = projectsUtilityService.saveProjectWithNameAndTeamLeadId("Test Project", teamLeadId);

        int applicantPersonId = personUtilityService.savePersonWithName("Applicant");

        String jwt = jwtUtilityService.generateTokenWithUserId(applicantPersonId);

        jdbcClient.sql("UPDATE project SET required_members_count = 5 WHERE id = :projectId")
        .param("projectId", projectId)
        .update();


        mockMvc.perform(
                        post("/applications")
                                .header("Authorization", "Bearer " + jwt)
                                .contentType("application/json")
                                .content("""
                                        {
                                            "project_id": %d
                                        }
                                        """
                                        .formatted(projectId))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));


        int actualAmountOfApplications = jdbcClient.sql("SELECT count(*) FROM application WHERE project_id = :projectId AND person_id = :personId")
                .param("projectId", projectId)
                .param("personId", applicantPersonId)
                .query(Integer.class)
                .single();
        assertThat(actualAmountOfApplications).isEqualTo(1);
    }
}
