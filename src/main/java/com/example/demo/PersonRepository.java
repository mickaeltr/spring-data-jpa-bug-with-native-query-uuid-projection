package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    @Query(value = "SELECT p.* FROM person p WHERE p.id = :#{#id}", nativeQuery = true)
    Person select(@Param("id") UUID id);

    @Query(value = "SELECT p.name FROM person p WHERE p.id = :#{#id}", nativeQuery = true)
    PersonName selectName(@Param("id") UUID id);

    @Query(value = "SELECT p.id FROM person p WHERE p.id = :#{#id}", nativeQuery = true)
    PersonId selectId(@Param("id") UUID id);

}
