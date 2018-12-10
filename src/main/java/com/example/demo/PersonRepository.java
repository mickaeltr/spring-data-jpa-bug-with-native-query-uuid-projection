package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    @Query(value = "SELECT p.name FROM person p LIMIT 1", nativeQuery = true)
    PersonName selectName();

    @Query(value = "SELECT p.id FROM person p LIMIT 1", nativeQuery = true)
    PersonId selectId();

}
