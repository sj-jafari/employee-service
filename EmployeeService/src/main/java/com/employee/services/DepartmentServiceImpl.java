package com.employee.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.domain.Department;
import com.employee.exceptions.DepartmentNotFoundException;
import com.employee.repositories.DepartmentRepository;

/**
 * This class provides an implementation for Services related to
 * {@link Department}
 * 
 * @author Jalal
 * @since 20190324
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
	@Autowired
	private DepartmentRepository departmentRepository;

	/**
	 * Saves a given {@link Department} into the DB.
	 * 
	 * @author Jalal
	 * @since 20190324
	 */
	public Department saveDepartment(Department department) {
		return departmentRepository.save(department);
	}

	/**
	 * Updates a given {@link Department} into the DB.
	 * 
	 * @author Jalal
	 * @since 20190324
	 */
	@Override
	public Department updateDepartment(Department department) {
		return departmentRepository.save(department);
	}

	/**
	 * Returns a {@link Department} given its ID. It the department is not found a
	 * proper exception is thrown.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @throws DepartmentNotFoundException
	 */
	@Override
	public Department getDepartmentById(Long id) throws DepartmentNotFoundException {
		Department department = departmentRepository.getById(id);
		if (department == null)
			throw new DepartmentNotFoundException(id);
		return department;
	}

	/**
	 * Returns a list of {@link Department} objects.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return List<Department>
	 */
	@Override
	public List<Department> getAllDepartments() {
		List<Department> departments = new ArrayList<>();
		departmentRepository.findAll().forEach(departments::add);

		return departments;
	}
}
