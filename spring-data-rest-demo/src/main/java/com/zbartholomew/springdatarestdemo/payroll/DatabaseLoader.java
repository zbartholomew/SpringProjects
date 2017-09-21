package com.zbartholomew.springdatarestdemo.payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * To work with this application, you need to pre-load
 * Implements Spring Boot’s CommandLineRunner so that it gets run after all the beans are created and registered.
 */

@Component // annotation so that it is automatically picked up by @SpringBootApplication
public class DatabaseLoader implements CommandLineRunner {

    private final EmployeeRepository repository;

    // Constructor injection and autowiring to get Spring Data’s automatically created EmployeeRepository
    @Autowired
    public DatabaseLoader(EmployeeRepository repository) {
        this.repository = repository;
    }

    // This method is invoked with command line arguments, loading up your data
    @Override
    public void run(String... strings) throws Exception {
        this.repository.save(new Employee("Zach", "Cheese", "Manager"));
    }
}
