package ru.teamsync.resume.integration.controller;

import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import ru.teamsync.resume.integration.IntegrationEnvironment;

public class ProfileControllerTest extends IntegrationEnvironment {

    @Test
    public void should_returnProfile_when_personExistsAndIsStudent() throws Exception {
        String email = "stud" + UUID.randomUUID().toString().substring(0, 8) + "@ex.com";
        int personId = jdbcClient
                .sql("INSERT INTO person(name, surname, email) VALUES ('test_student', 'test_student', :email) RETURNING id")
                .param("email", email)
                .query(Integer.class).single();
        
        
        int studentId = studentUtilityService.createStudentWithPersonId(personId);
        String jwt = jwtUtilityService.generateTokenWithUserId(personId);

        mockMvc.perform(
                    get("/profile/{personId}", personId)
                            .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.type").value("STUDENT"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    public void should_returnProfile_when_personExistsAndIsProfessor() throws Exception {
        String email = "prof" + UUID.randomUUID().toString().substring(0, 8) + "@ex.com";
        int personId = jdbcClient
                .sql("INSERT INTO person(name, surname, email) VALUES ('test_professor', 'test_professor', :email) RETURNING id")
                .param("email", email)
                .query(Integer.class).single();
        
        
        int professorId = professorUtilityService.createProfessorWithPersonId(personId);
        String jwt = jwtUtilityService.generateTokenWithUserId(personId);

        mockMvc.perform(
                    get("/profile/{personId}", personId)
                            .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.type").value("PROFESSOR"))
                .andExpect(jsonPath("$.data").exists());
    }
}
