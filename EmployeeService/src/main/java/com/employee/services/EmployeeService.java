package com.employee.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.employee.domain.Employee;
import com.employee.exceptions.DepartmentNotFoundException;
import com.employee.exceptions.EmployeeNotFoundException;

/**
 * A general interface for definition of services available for {@link Employee}
 * 
 * @author Jalal
 * @since 20190324
 */
@Service
public interface EmployeeService {
	/**
	 * Saves a given {@link Employee} into the DB.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return Employee
	 */
	public Employee saveEmployee(Employee employee) throws DepartmentNotFoundException;

	/**
	 * Updates a given {@link Employee} into the DB.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return Employee
	 */
	public Employee updateEmployee(Employee employee) throws EmployeeNotFoundException, DepartmentNotFoundException;

	/**
	 * Deletes an {@link Employee} given its ID.
	 * 
	 * @author Jalal
	 * @since 20190324
	 */
	public void deleteEmployeeById(String uuid) throws EmployeeNotFoundException;

	/**
	 * Returns an {@link Employee} given its ID.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return Employee
	 */
	public Employee getEmployeeById(String uuid) throws EmployeeNotFoundException;

	/**
	 * Returns list of all {@link Employee} objects from DB.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return List<Employee>
	 */
	public List<Employee> getAllEmployees();

}
