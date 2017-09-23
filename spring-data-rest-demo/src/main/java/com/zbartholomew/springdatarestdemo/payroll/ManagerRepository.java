package com.zbartholomew.springdatarestdemo.payroll;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// Spring Data REST, by default, will export any repository it finds.
// We do NOT want this repository exposed for REST operations
@RepositoryRestResource(exported = false) // block from export
public interface ManagerRepository extends Repository<Manager, Long> {
    Manager save(Manager manager);
    Manager findByName(String name);
}
