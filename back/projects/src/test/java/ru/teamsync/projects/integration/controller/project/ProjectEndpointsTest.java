package ru.teamsync.projects.integration.controller.project;

import org.junit.jupiter.api.Test;
import ru.teamsync.projects.dto.request.ProjectUpdateRequest;
import ru.teamsync.projects.integration.IntegrationEnvironment;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectEndpointsTest extends IntegrationEnvironment {

    @Test
    public void should_returnOnlyProjectsForCurrentUser_when_requested() throws Exception {
        int currentUserId = personUtilityService.savePersonWithName("Person1");
        int anotherUserId = personUtilityService.savePersonWithName("Person2");

        int currentUserProject1Id = projectsUtilityService.saveProjectWithNameAndTeamLeadId("Project1", currentUserId);
        int currentUserProject2Id = projectsUtilityService.saveProjectWithNameAndTeamLeadId("Project2", currentUserId);

        projectsUtilityService.saveProjectWithNameAndTeamLeadId("Project3", anotherUserId);

        String jwt = jwtUtilityService.generateTokenWithUserId(currentUserId);


        mockMvc.perform(
                        get("/projects/my")
                                .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.content[0].id").value(currentUserProject1Id))
                .andExpect(jsonPath("$.data.content[1].id").value(currentUserProject2Id));
    }

    @Test
    public void should_returnForbidden_when_deleteSomeoneElseProject() throws Exception {
        int currentUserId = personUtilityService.savePersonWithName("Person1");
        int anotherUserId = personUtilityService.savePersonWithName("Person2");

        int anotherUserProjectId = projectsUtilityService.saveProjectWithNameAndTeamLeadId("Project1", anotherUserId);

        String jwt = jwtUtilityService.generateTokenWithUserId(currentUserId);


        mockMvc.perform(
                        delete("/projects/{projectId}", anotherUserProjectId)
                                .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void should_returnForbidden_when_updateSomeoneElseProject() throws Exception {
        int currentUserId = personUtilityService.savePersonWithName("Person1");
        int anotherUserId = personUtilityService.savePersonWithName("Person2");

        int anotherUserProjectId = projectsUtilityService.saveProjectWithNameAndTeamLeadId("Project1", anotherUserId);

        String jwt = jwtUtilityService.generateTokenWithUserId(currentUserId);

        var projectUpdateRequest = new ProjectUpdateRequest(
                "Updated Project",
                "Updated Course",
                "Updated Description",
                "https://updated-link.com",
                "ACTIVE",
                null,
                null,
                5
        );

        mockMvc.perform(
                        put("/projects/{projectId}", anotherUserProjectId)
                                .header("Authorization", "Bearer " + jwt)
                                .contentType("application/json")
                                .content("""
                                        {
                                            "name": "123",
                                            "course_name": "123",
                                            "description": "123",
                                            "project_link": "123",
                                            "status": "ACTIVE",
                                            "skills": null,
                                            "roles": null,
                                            "required_members_count": 4
                                        }
                                        """)
                )
                .andExpect(status().isForbidden());
    }


}
