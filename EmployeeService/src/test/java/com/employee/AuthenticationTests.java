package com.employee;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.employee.authentication.Authenticator;
import com.employee.authentication.AuthenticatorImpl;
import com.employee.exceptions.InvalidCredentialsException;
import com.employee.exceptions.InvalidTokenException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthenticatorImpl.class)
public class AuthenticationTests {

	@Autowired
	private Authenticator authenticator;
	private String validToken;

	@Before
	public void initialize() throws InvalidCredentialsException {
		validToken = authenticator.login("admin", "admin");
	}

	@Test
	public void loginWithCorrectCredentials() throws InvalidCredentialsException {
		String token = authenticator.login("admin", "admin");
		assertNotNull(token);
		assertNotEquals(0, token.length());
	}

	@Test(expected = InvalidCredentialsException.class)
	public void loginWithIncorrectCredentials() throws InvalidCredentialsException {
		authenticator.login("nothing", "nothing");
	}

	@Test
	public void checkCorrectToken() throws InvalidCredentialsException, InvalidTokenException {
		boolean result = authenticator.isTokenValid(validToken);
		assertTrue(result);
	}

	@Test(expected = InvalidTokenException.class)
	public void checkIncorrectToken() throws InvalidCredentialsException, InvalidTokenException {
		authenticator.isTokenValid("nothing");
	}

}
