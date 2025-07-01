package ru.teamsync.resume.integration;

import groovy.util.logging.Log4j2;
import ru.teamsync.resume.integration.utils.JwtUtilityService;
import ru.teamsync.resume.integration.utils.StudentUtilityService;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.LabelExpression;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.DriverManager;
import java.util.List;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yaml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Log4j2
public abstract class IntegrationEnvironment {

    private final static List<String> TABLES = List.of("person",
        "study_group",
        "student",
        "professor",
        "project",
        "project_member",
        "application",
        "student_favourite_project",
        "skill",
        "student_skill",
        "role",
        "project_role",
        "student_role",
        "project_skill"
    );

    @ServiceConnection
    protected static PostgreSQLContainer<?> POSTGRES;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected JdbcClient jdbcClient;

    @Autowired
    protected JwtUtilityService jwtUtilityService;

    @Autowired
    protected StudentUtilityService studentUtilityService;

    static {
        try {
            setUpPostgres();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize PostgreSQL container", e);
        }
    }

    private static void setUpPostgres() {
        POSTGRES = new PostgreSQLContainer<>("postgres:16")
                .withDatabaseName("team-sync-test")
                .withUsername("test-user")
                .withPassword("test-password");
        POSTGRES.start();
    }

    @BeforeEach
    void tearDownTables() {
        String tables = String.join(", ", TABLES);
        jdbcClient.sql("TRUNCATE TABLE " + tables + " RESTART IDENTITY CASCADE");
    }
}
