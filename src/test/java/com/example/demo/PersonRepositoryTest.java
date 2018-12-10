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
    void findById() {
        // Given
        Person p = new Person();
        p.setName("Mickey");
        UUID id = personRepository.save(p).getId();

        // When
        Person person = personRepository.findById(id).get();

        // Then
        Assertions.assertEquals(id, person.getId());
        Assertions.assertEquals("Mickey", person.getName());
    }

    @Test
    void selectName() {
        // Given
        Person p = new Person();
        p.setName("Donald");
        personRepository.save(p);

        // When
        PersonName personName = personRepository.selectName();

        // Then
        Assertions.assertEquals("Donald", personName.getName());
    }

    @Test
    void selectId() {
        // Given
        Person p = new Person();
        p.setName("Daisy");
        UUID id = personRepository.save(p).getId();

        // When
        PersonId personId = personRepository.selectId();

        // Then
        Assertions.assertEquals(id, personId.getId());
    }
}
