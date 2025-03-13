package com.registration.registration.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.registration.registration.model.Person;
import com.registration.registration.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/api/people")
@CrossOrigin(origins = "http://localhost:4200")
public class PersonController {
    
    private final PersonService personService;
    
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }
    
    @GetMapping
    public ResponseEntity<List<Person>> getAllPeople() {
        List<Person> people = personService.getAllPeople();
        return new ResponseEntity<>(people, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id)
                .map(person -> new ResponseEntity<>(person, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person savedPerson = personService.savePerson(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person person) {
        return personService.getPersonById(id)
                .map(existingPerson -> {
                    person.setId(id);
                    Person updatedPerson = personService.savePerson(person);
                    return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        return personService.getPersonById(id)
                .map(person -> {
                    personService.deletePerson(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Person>> searchPeople(@RequestParam String query) {
        List<Person> people = personService.searchPeople(query);
        return new ResponseEntity<>(people, HttpStatus.OK);
    }
}