package com.employee.controller;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.authentication.Authenticator;
import com.employee.authentication.LoginInfo;
import com.employee.domain.Department;
import com.employee.domain.Employee;
import com.employee.exceptions.DepartmentNotFoundException;
import com.employee.exceptions.EmployeeNotFoundException;
import com.employee.exceptions.InvalidCredentialsException;
import com.employee.exceptions.InvalidTokenException;
import com.employee.services.DepartmentService;
import com.employee.services.EmployeeService;

@RestController
@RequestMapping("/api/v1")
public class EmployeeServiceController {
	private final Logger logger = LoggerFactory.getLogger(EmployeeServiceController.class);
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private Authenticator authenticator;

	@GetMapping("/hello")
	public ResponseEntity<?> getHelloMessage() {
		String message = "Hello!";
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody LoginInfo loginInfo) throws InvalidCredentialsException {

		String token = authenticator.login(loginInfo.getUsername(), loginInfo.getPassword());
		JSONObject message = new JSONObject();
		message.put("token", token);
		return new ResponseEntity<String>(message.toString(), HttpStatus.OK);
	}

	@PostMapping(path = "/employee", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createEmployee(@Valid @RequestBody Employee employee,
			@RequestHeader(value = "token") String token) throws DepartmentNotFoundException, InvalidTokenException {
		authenticator.isTokenValid(token);
		logger.info("Request to create employee: {}", employee);
		Employee result = employeeService.saveEmployee(employee);
		return new ResponseEntity<Employee>(result, HttpStatus.CREATED);
	}

	@GetMapping(path = "/employee", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllEmployees(@RequestHeader(value = "token") String token)
			throws EmployeeNotFoundException, InvalidTokenException {
		authenticator.isTokenValid(token);
		logger.info("Request to get all employees");
		List<Employee> result = employeeService.getAllEmployees();
		return new ResponseEntity<List<Employee>>(result, HttpStatus.OK);
	}

	@GetMapping(path = "/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEmployeeById(@PathVariable(value = "id") String id,
			@RequestHeader(value = "token") String token) throws EmployeeNotFoundException, InvalidTokenException {
		authenticator.isTokenValid(token);
		logger.info("Request to get employee: {}", id);
		Employee result = employeeService.getEmployeeById(id);
		return new ResponseEntity<Employee>(result, HttpStatus.OK);
	}

	@PutMapping("/employee")
	public ResponseEntity<?> updateEmployee(@Valid @RequestBody Employee employee,
			@RequestHeader(value = "token") String token)
			throws EmployeeNotFoundException, DepartmentNotFoundException, InvalidTokenException {
		authenticator.isTokenValid(token);
		logger.info("Request to update employee: {}", employee);
		Employee result = employeeService.updateEmployee(employee);
		return new ResponseEntity<Employee>(result, HttpStatus.OK);
	}

	@DeleteMapping("/employee/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable(value = "id") String id,
			@RequestHeader(value = "token") String token) throws EmployeeNotFoundException, InvalidTokenException {
		authenticator.isTokenValid(token);
		logger.info("Request to delete employee: {}", id);
		employeeService.deleteEmployeeById(id);
		return new ResponseEntity<Employee>(HttpStatus.OK);
	}

	@PostMapping("/department")
	public ResponseEntity<?> createDepartment(@RequestBody Department department,
			@RequestHeader(value = "token") String token) throws URISyntaxException, InvalidTokenException {
		authenticator.isTokenValid(token);
		logger.info("Request to create department: {}", department);
		Department result = departmentService.saveDepartment(department);
		return new ResponseEntity<Department>(result, HttpStatus.CREATED);
	}
}
