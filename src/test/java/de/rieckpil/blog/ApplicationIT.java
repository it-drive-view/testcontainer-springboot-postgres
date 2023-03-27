package de.rieckpil.blog;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

// JUnit 5 example with Spring Boot >= 2.2.6
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationIT {

    private final Logger logger = LoggerFactory.getLogger(ApplicationIT.class);

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
            .withPassword("inmemory")
            .withUsername("inmemory")
            .withDatabaseName("test-database");

    // TODO à quoi ça sert ça ?
    //            .withCommand("top");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Test
    public void contextLoads() {
        assertTrue(postgreSQLContainer.isRunning());
        logger.info("containerId de mon container: " + postgreSQLContainer.getContainerId());

        try {
            org.testcontainers.containers.Container.ExecResult result = postgreSQLContainer.execInContainer("ls", "-al");
            logger.info("======================================================================================= result.toString()");
            logger.info(result.toString());
            logger.info("======================================================================================= result.getStdout()");
            logger.info(result.getStdout());
            logger.info("======================================================================================= result.getStdout()");

            org.testcontainers.containers.Container.ExecResult env = postgreSQLContainer.execInContainer("env");
            logger.info("======================================================================================= env");
            logger.info(env.getStdout());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.info(postgreSQLContainer.getJdbcUrl());
        logger.info(postgreSQLContainer.getUsername());
        logger.info(postgreSQLContainer.getPassword());
    }

}
