package com.employee.exceptions;

/**
 * This exception occurs when a department with the given information is not
 * found.
 * 
 * @author Jalal
 * @since 20190324
 */
public class DepartmentNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	private static String message = "The given department was not found";

	public DepartmentNotFoundException(Long id) {
		super(message + ": " + id);
	}
}
