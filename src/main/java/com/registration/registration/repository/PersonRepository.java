package com.registration.registration.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.registration.registration.model.Person;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
    @Query("SELECT p FROM Person p WHERE CAST(p.id AS string) LIKE %:query% OR p.name LIKE %:query%")
    List<Person> searchByIdOrName(@Param("query") String query);
}