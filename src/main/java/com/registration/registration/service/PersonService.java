package com.registration.registration.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registration.registration.model.Person;
import com.registration.registration.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    
    private final PersonRepository personRepository;
    
    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    
    public List<Person> getAllPeople() {
        return personRepository.findAll();
    }
    
    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }
    
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }
    
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }
    
    public List<Person> searchPeople(String query) {
        return personRepository.searchByIdOrName(query);
    }
}