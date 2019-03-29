package com.employee.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Used as a DTO to carry login credential info
 * @author Jalal
 * @since 20190324
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {
	private String username;
	private String password;
}
