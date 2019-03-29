package com.employee.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.employee.domain.Department;
import com.employee.exceptions.DepartmentNotFoundException;

/**
 * A general interface for definition of services available for
 * {@link Department}
 * 
 * @author Jalal
 * @since 20190324
 */
@Service
public interface DepartmentService {
	/**
	 * Saves a given {@link Department} into the DB.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return Department
	 */
	public Department saveDepartment(Department department);

	/**
	 * Updates a given {@link Department} on the DB.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return Department
	 */
	public Department updateDepartment(Department department);

	/**
	 * Returns a {@link Department} given its ID.
	 * 
	 * @author Jalal
	 * @since 20190324
	 */
	public Department getDepartmentById(Long id) throws DepartmentNotFoundException;
	
	/**
	 * Returns list of all {@link Department} objects from DB.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return List<Department>
	 */
	public List<Department> getAllDepartments();

}
