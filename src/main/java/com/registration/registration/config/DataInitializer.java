package com.registration.registration.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.registration.registration.model.Person;
import com.registration.registration.repository.PersonRepository;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(PersonRepository repository) {
        return args -> {
            // Only add sample data if the repository is empty
            if (repository.count() == 0) {
                repository.save(new Person(
                    "John Doe", 
                    "12/ABC(N)123456", 
                    LocalDate.of(1990, 1, 1),
                    "Richard Doe",
                    "123456789",
                    "john@example.com",
                    "Township 1",
                    "123 Main St"
                ));
                
                repository.save(new Person(
                    "Jane Smith", 
                    "10/XYZ(N)654321", 
                    LocalDate.of(1992, 5, 15),
                    "Robert Smith",
                    "987654321",
                    "jane@example.com",
                    "Township 2",
                    "456 Oak Ave"
                ));
                
                repository.save(new Person(
                    "Mike Johnson", 
                    "09/MNO(N)789012", 
                    LocalDate.of(1985, 11, 30),
                    "David Johnson",
                    "456789123",
                    "mike@example.com",
                    "Township 3",
                    "789 Pine Rd"
                ));
                
                System.out.println("Sample data initialized");
            }
        };
    }
}