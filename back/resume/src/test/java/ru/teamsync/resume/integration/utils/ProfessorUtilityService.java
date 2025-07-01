package ru.teamsync.resume.integration.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
public class ProfessorUtilityService {

    @Autowired
    private JdbcClient jdbcClient;

    public int createProfessorWithPersonId(int personId) {
        return jdbcClient
                .sql("INSERT INTO professor (person_id, tg_alias) VALUES (:person_id, :tg_alias) RETURNING id")
                .param("person_id", personId)
                .param("tg_alias", "test-tg-alias-" + personId)
                .query(Integer.class)
                .single();
    }
}
