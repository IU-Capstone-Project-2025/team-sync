package ru.teamsync.resume.integration.controller;

import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ru.teamsync.resume.integration.IntegrationEnvironment;

public class ProfileControllerTest extends IntegrationEnvironment {

    @Test
    public void should_returnProfile_when_personExistsAndIsStudent() throws Exception {
        int personId = jdbcClient
                .sql("INSERT INTO person(name, surname, email) VALUES ('test', 'test', 'testmail') RETURNING id")
                .query(Integer.class).single();
        
        
        int studentId = studentUtilityService.createStudentWithPersonId(personId);
        String jwt = jwtUtilityService.generateTokenWithUserId(studentId);

        mockMvc.perform(
                    get("/profile/{personId}", studentId)
                            .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.type").value("student"))
                .andExpect(jsonPath("$.data").exists());
    }
}
