package com.employee.authentication;

import java.util.HashMap;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.employee.exceptions.InvalidCredentialsException;
import com.employee.exceptions.InvalidTokenException;

/**
 * This class provides a simple in memory method to authenticate users
 * 
 * @author Jalal
 * @since 20190324
 * @version 1.0
 */
@Repository
public class AuthenticatorImpl implements Authenticator {
	private HashMap<String, String> userTokenMap;

	/**
	 * Initializes the object.
	 * 
	 * @author Jalal
	 * @since 20190324
	 */
	@PostConstruct
	private void initialize() {
		userTokenMap = new HashMap<String, String>();
	}

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
	@Override
	public synchronized String login(String username, String password) throws InvalidCredentialsException {
		if (username.equals("admin") && password.equals("admin")) { //TODO hard coded for simplicity
			String token = "";
			if (userTokenMap.containsKey(username)) {
				token = userTokenMap.get(username);
			} else {
				token = UUID.randomUUID().toString();
				userTokenMap.put(username, token);
			}
			return token;
		} else { // incorrect credentials
			throw new InvalidCredentialsException();
		}
	}

	/**
	 * Given a correct token and username, confirms its validity.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @param username
	 * @param token
	 * @throws InvalidTokenException
	 */
	@Override
	public boolean isTokenValid(String token) throws InvalidTokenException {
		if (userTokenMap.values().contains(token))
			return true;
		else
			throw new InvalidTokenException();
	}

}
