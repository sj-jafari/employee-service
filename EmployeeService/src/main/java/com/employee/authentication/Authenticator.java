package com.employee.authentication;

import com.employee.exceptions.InvalidCredentialsException;
import com.employee.exceptions.InvalidTokenException;

/**
 * A common interface for authenticating users.
 * 
 * @author Jalal
 * @since 20190324
 */
public interface Authenticator {
	/**
	 * Given correct credentials, logs a user in and returns a token for further
	 * calls.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @param username
	 * @param password
	 * @throws InvalidCredentialsException
	 */
	public String login(String username, String password) throws InvalidCredentialsException;

	/**
	 * Given a correct token and username, confirms its validity.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @param username
	 * @param token
	 * @throws InvalidTokenException
	 */
	public boolean isTokenValid(String token) throws InvalidTokenException;
}
