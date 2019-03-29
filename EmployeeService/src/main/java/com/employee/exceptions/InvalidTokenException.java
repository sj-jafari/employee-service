package com.employee.exceptions;

/**
 * This exception occurs when a token is not valid.
 * 
 * @author Jalal
 * @since 20190324
 */
public class InvalidTokenException extends Exception {
	private static final long serialVersionUID = 1L;
	private static final String message = "Invalid token!";

	public InvalidTokenException() {
		super(message);
	}
}
