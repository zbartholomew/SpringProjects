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

    // Constructor injection and autowiring to get Spring Data automatically created EmployeeRepository
    @Autowired
    public DatabaseLoader(EmployeeRepository repository) {
        this.repository = repository;
    }

    // This method is invoked with command line arguments, loading up the data
    @Override
    public void run(String... strings) throws Exception {
        this.repository.save(new Employee("Zach", "Cheese", "Manager"));
        this.repository.save(new Employee("John", "Smith", "Employee"));
        this.repository.save(new Employee("Bob", "Thorton", "Driver"));
        this.repository.save(new Employee("Bill", "Pots", "Receptionist"));
        this.repository.save(new Employee("Joe", "Pechy", "Security"));
        this.repository.save(new Employee("Kevin", "Freeton", "CEO"));
    }
}
