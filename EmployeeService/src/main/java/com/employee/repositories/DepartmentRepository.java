package com.employee.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.employee.domain.Department;

/**
 * A general interface to perform crud operations on {@link Department}
 * 
 * @author Jalal
 * @since 20190324
 */
@Repository
public interface DepartmentRepository extends CrudRepository<Department, Long> {
	public Department getById(Long id);
}
