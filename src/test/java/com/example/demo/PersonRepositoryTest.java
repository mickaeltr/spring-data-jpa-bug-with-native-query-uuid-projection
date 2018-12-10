package com.example.demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {PersonRepositoryTest.Initializer.class})
class PersonRepositoryTest {

    private static final PostgreSQLContainer DB = new PostgreSQLContainer("postgres:latest");

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + DB.getJdbcUrl(),
                    "spring.datasource.username=" + DB.getUsername(),
                    "spring.datasource.password=" + DB.getPassword()
            ).applyTo(applicationContext);
        }
    }

    @BeforeAll
    static void beforeAll() {
        DB.start();
    }

    @AfterAll
    static void afterAll() {
        DB.stop();
    }

    @Autowired
    private PersonRepository personRepository;

    @Test
    void selectName() {
        Person p = new Person();
        p.setName("Donald");
        personRepository.save(p);
        PersonName personNames = personRepository.selectName();
        Assertions.assertEquals("Donald", personNames.getName());
    }

    @Test
    void selectId() {
        Person p = new Person();
        p.setName("Donald");
        UUID id = personRepository.save(p).getId();
        PersonId personId = personRepository.selectId();
        Assertions.assertEquals(id, personId.getId());
    }
}
