package com.zbartholomew.springdatarestdemo.payroll;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * This entity is used to track employee information. In this case, their name and job description.
 */

@Data // Autogenerates getters, setters, constructors, toString, hash, equals, and other things.
@Entity // JPA annotation that denotes the whole class for storage in a relational table
public class Employee {

    @Id
    @GeneratedValue // JPA annotations to note the primary key and that is generated automatically when needed.
    private Long id;
    private String firstName;
    private String lastName;
    private String description;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
    }
}
