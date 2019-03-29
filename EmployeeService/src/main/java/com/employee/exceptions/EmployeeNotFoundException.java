package com.employee.exceptions;

/**
 * This exception occurs when an employee with the given information is not
 * found.
 * 
 * @author Jalal
 * @since 20190324
 */
public class EmployeeNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;
	private static String message = "The given employee was not found";

	public EmployeeNotFoundException(String uuid) {
		super(message + ": " + uuid);
	}
}
