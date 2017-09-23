package com.zbartholomew.springdatarestdemo.payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * To work with this application, you need to pre-load
 * Implements Spring Boot’s CommandLineRunner so that it gets run after all the beans are created and registered.
 */

@Component // annotation so that it is automatically picked up by @SpringBootApplication
public class DatabaseLoader implements CommandLineRunner {

    private final EmployeeRepository employees;
    private final ManagerRepository managers;

    // Constructor injection and autowiring to get Spring Data automatically created EmployeeRepository
    @Autowired
    public DatabaseLoader(EmployeeRepository employeeRepository,
                          ManagerRepository managerRepository) {

        this.employees = employeeRepository;
        this.managers = managerRepository;
    }

    // This method is invoked with command line arguments, loading up the data
    @Override
    public void run(String... strings) throws Exception {
        Manager zach = this.managers.save(new Manager("zach", "password",
                "ROLE_MANAGER"));
        Manager bob = this.managers.save(new Manager("bob", "password",
                "ROLE_MANAGER"));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("zach", "password",
                        AuthorityUtils.createAuthorityList("ROLE_MANAGER")));

        this.employees.save(new Employee("Bill", "Excellent", "Rocker 1", zach));
        this.employees.save(new Employee("Ted", "Adventure", "Rocker 2", zach));
        this.employees.save(new Employee("Joe", "Bill", "Something", zach));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("bob", "password",
                        AuthorityUtils.createAuthorityList("ROLE_MANAGER")));

        this.employees.save(new Employee("Jean", "Luc", "Captain", bob));
        this.employees.save(new Employee("Will", "Riker", "First Officer", bob));
        this.employees.save(new Employee("Wesley", "Crusher", "Annoying Kid", bob));

        SecurityContextHolder.clearContext();
    }
}
