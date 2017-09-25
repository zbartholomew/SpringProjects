package com.zbartholomew.springdatarestdemo.payroll;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * The repository extends PagingAndSortingRepository which adds extra options to set page size,
 * and also adds navigational links to hop from page to page.
 */

public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long> {
}
