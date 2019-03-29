package com.employee.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.employee.domain.Employee;
/**
 * A general interface to perform crud operations on {@link Employee}
 * 
 * @author Jalal
 * @since 20190324
 */
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, String> {
	public Employee getById(String id); 
}
