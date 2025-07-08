package ru.teamsync.projects.integration.controller.project;

import org.junit.jupiter.api.Test;
import ru.teamsync.projects.integration.IntegrationEnvironment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectsFilteringTest extends IntegrationEnvironment {


    @Test
    public void should_returnFilteredProjects_when_filterBySkills() throws Exception {
        int skill1Id = jdbcClient.sql(
                "INSERT INTO skill(name, description) VALUES ('skill 1', 'desc 1') RETURNING id"
        ).query(Integer.class).single();

        int skill2Id = jdbcClient.sql(
                "INSERT INTO skill(name, description) VALUES ('skill 2', 'desc 2') RETURNING id"
        ).query(Integer.class).single();

        int teamLeadId = personUtilityService.savePersonWithName("person");

        int project1Id = projectsUtilityService.saveProjectWithNameAndTeamLeadId("project", teamLeadId);
        int project2Id = projectsUtilityService.saveProjectWithNameAndTeamLeadId("project2", teamLeadId);

        jdbcClient.sql("INSERT INTO project_skill(project_id, skill_id) VALUES (:projectId, :skillId)")
                .param("projectId", project1Id)
                .param("skillId", skill1Id)
                .update();
        jdbcClient.sql("INSERT INTO project_skill(project_id, skill_id) VALUES (:projectId, :skillId)")
                .param("projectId", project2Id)
                .param("skillId", skill2Id)
                .update();

        String jwt = jwtUtilityService.generateTokenWithUserId(teamLeadId);


        mockMvc.perform(
                        get("/projects")
                                .header("Authorization", "Bearer " + jwt)
                                .queryParam("skillIds", String.valueOf(skill1Id))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(project1Id));

    }

    @Test
    public void should_returnFilteredProjects_when_filterByRoles() throws Exception {
        int role1Id = jdbcClient.sql(
                "INSERT INTO role(name, description) VALUES ('role 1', 'desc 1') RETURNING id"
        ).query(Integer.class).single();

        int role2Id = jdbcClient.sql(
                "INSERT INTO role(name, description) VALUES ('role 2', 'desc 2') RETURNING id"
        ).query(Integer.class).single();

        int teamLeadId = personUtilityService.savePersonWithName("person");

        int project1Id = projectsUtilityService.saveProjectWithNameAndTeamLeadId("project", teamLeadId);
        int project2Id = projectsUtilityService.saveProjectWithNameAndTeamLeadId("project2", teamLeadId);

        jdbcClient.sql("INSERT INTO project_role(project_id, role_id) VALUES (:projectId, :roleId)")
                .param("projectId", project1Id)
                .param("roleId", role1Id)
                .update();
        jdbcClient.sql("INSERT INTO project_role(project_id, role_id) VALUES (:projectId, :roleId)")
                .param("projectId", project2Id)
                .param("roleId", role2Id)
                .update();

        String jwt = jwtUtilityService.generateTokenWithUserId(teamLeadId);

        mockMvc.perform(
                        get("/projects")
                                .header("Authorization", "Bearer " + jwt)
                                .queryParam("roleIds", String.valueOf(role1Id))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(project1Id));
    }

    @Test
    public void should_returnFilteredProjects_when_filterByStatus() throws Exception {
        int teamLeadId = personUtilityService.savePersonWithName("person");

        int project1Id = projectsUtilityService.saveProjectWithNameAndTeamLeadId("project", teamLeadId);
        int project2Id = projectsUtilityService.saveProjectWithNameAndTeamLeadId("project2", teamLeadId);

        jdbcClient.sql("UPDATE project SET status = 'ACTIVE' WHERE id = :projectId")
                .param("projectId", project1Id)
                .update();
        jdbcClient.sql("UPDATE project SET status = 'DRAFT' WHERE id = :projectId")
                .param("projectId", project2Id)
                .update();

        String jwt = jwtUtilityService.generateTokenWithUserId(teamLeadId);

        mockMvc.perform(
                        get("/projects")
                                .header("Authorization", "Bearer " + jwt)
                                .queryParam("status", "ACTIVE")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(project1Id));
    }

    @Test
    public void should_returnFilteredProjects_when_filterByCourseName() throws Exception {
        int teamLeadId = personUtilityService.savePersonWithName("person");

        int project1Id = projectsUtilityService.saveProjectWithNameAndTeamLeadId("project", teamLeadId);
        int project2Id = projectsUtilityService.saveProjectWithNameAndTeamLeadId("project2", teamLeadId);

        jdbcClient.sql("UPDATE project SET course_name = 'Course 1' WHERE id = :projectId")
                .param("projectId", project1Id)
                .update();
        jdbcClient.sql("UPDATE project SET course_name = 'Course 2' WHERE id = :projectId")
                .param("projectId", project2Id)
                .update();

        String jwt = jwtUtilityService.generateTokenWithUserId(teamLeadId);

        mockMvc.perform(
                        get("/projects")
                                .header("Authorization", "Bearer " + jwt)
                                .queryParam("courseName", "Course 1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(project1Id));
    }

}
