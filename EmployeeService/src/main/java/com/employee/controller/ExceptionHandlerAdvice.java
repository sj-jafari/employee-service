package com.employee.controller;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.employee.exceptions.InvalidCredentialsException;
import com.employee.exceptions.InvalidTokenException;

/**
 * Exceptions thrown from controller layer are handled through this class. Each
 * Exception-specific method, generally creates a json response indicating error
 * detail and returns it with the proper error code.
 * 
 * @author Jalal
 * @since 20190324
 * @version 1.0
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<?> handleException(InvalidTokenException e) {
		// log exception
		JSONObject errorJson = new JSONObject();
		errorJson.put("message", e.getMessage());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(errorJson.toString(), headers, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<?> handleException(InvalidCredentialsException e) {
		// log exception
		JSONObject errorJson = new JSONObject();
		errorJson.put("message", e.getMessage());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(errorJson.toString(), headers, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<?> handleException(ConstraintViolationException e) {
		// log exception
		JSONObject errorJson = new JSONObject();
		errorJson.put("message", "Data Integrity Violation");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(errorJson.toString(), headers, HttpStatus.BAD_REQUEST);
	}

	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {

		BindingResult result = ex.getBindingResult();
		List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();
		JSONObject errorJson = new JSONObject();
		errorJson.put("message", "Invalid input data!");
		errorJson.put("target entity", result.getObjectName());

		JSONArray errors = new JSONArray();
		for (FieldError error : fieldErrors) {
			errors.put("'" + error.getField() + "' " + error.getDefaultMessage());
		}
		errorJson.put("errors", errors);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(errorJson.toString(), headers, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e) {
		// log exception
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		JSONObject errorJson = new JSONObject();
		errorJson.put("message", e.getMessage());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(errorJson.toString(), headers, HttpStatus.BAD_REQUEST);
	}
}