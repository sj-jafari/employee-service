package com.employee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.employee.domain.Department;
import com.employee.domain.Employee;
import com.employee.exceptions.DepartmentNotFoundException;
import com.employee.exceptions.EmployeeNotFoundException;
import com.employee.services.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { EmployeeServiceApplication.class })
//@ActiveProfiles("test")
public class EmployeeServiceTests {

	@Autowired
	private EmployeeService employeeService;
	private List<Employee> initialEmployeeslist;
	private Validator validator;

	@Before
	public void init() throws EmployeeNotFoundException {
		// init employees
		initialEmployeeslist = employeeService.getAllEmployees();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testInitialDataExistence() {
		assertNotNull(initialEmployeeslist);
		assertNotEquals(0, initialEmployeeslist.size());
	}

	// ----- SAVE TESTS
	@Test
	public void testSaveEmployee() throws DepartmentNotFoundException {

		Employee employee = new Employee(null, "Jane Doe", "jane@doe.com", "1989-01-01", null);
		Employee employeeFromDB = employeeService.saveEmployee(employee);

		assertEquals(employee.getEmail(), employeeFromDB.getEmail());
		assertEquals(employee.getName(), employeeFromDB.getName());
		assertEquals(employee.getBirthday(), employeeFromDB.getBirthday());

		Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void testSaveEmployeeWithValidDepartment() throws DepartmentNotFoundException {

		Employee employee = new Employee(null, "Jack Doe", "Jack@doe.com", "1979-01-01",
				new Department((long) 1, "IT"));
		Employee employeeFromDB = employeeService.saveEmployee(employee);

		assertEquals(employee.getEmail(), employeeFromDB.getEmail());
		assertEquals(employee.getName(), employeeFromDB.getName());
		assertEquals(employee.getBirthday(), employeeFromDB.getBirthday());
		assertNotNull(employee.getDepartment());
	}

	@Test(expected = DepartmentNotFoundException.class)
	public void testSaveEmployeeWithInvalidDepartment() throws DepartmentNotFoundException {

		Employee empOne = new Employee(null, "Jane Doe", "jane@doe.com", "1989-01-01",
				new Department((long) 99, "NONE"));
		employeeService.saveEmployee(empOne);
	}

	// VALIDATION TESTS
	//TODO non unique email
	
	@Test()
	public void testValidateEmployeeWithInvalidEmail() throws DepartmentNotFoundException {

		Employee employee = new Employee(null, "Jane Doe", "NOT A VALID EMAIL", "1989-01-01", null);
		Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
		assertFalse(violations.isEmpty());
	}

	@Test()
	public void testValidateEmployeeWithInvalidBirthday() throws DepartmentNotFoundException {
		// birthday should be in yyyy-mm-dd format
		Employee employee = new Employee(null, "Jane Doe", "jane@doe.com", "19890101", null);
		Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
		assertFalse(violations.isEmpty());
	}

	// ----- UPDATE TESTS
	@Test
	public void testUpdateEmployee() throws DepartmentNotFoundException, EmployeeNotFoundException {
		Employee employeeToUpdate = initialEmployeeslist.get(0);
		employeeToUpdate.setName("Test Name");
		employeeToUpdate.setEmail("test@test.com");
		employeeToUpdate.setBirthday("1900-01-01");
		employeeToUpdate.setDepartment(null);

		Employee employeeFromDB = employeeService.updateEmployee(employeeToUpdate);

		assertEquals("Test Name", employeeFromDB.getName());
		assertEquals("test@test.com", employeeFromDB.getEmail());
		assertEquals("1900-01-01", employeeFromDB.getBirthday());
		assertNull(employeeFromDB.getDepartment());
	}

	@Test(expected = EmployeeNotFoundException.class)
	public void testUpdateEmployeeWithInvalidEmployeeInfo()
			throws DepartmentNotFoundException, EmployeeNotFoundException {

		Employee employeeToUpdate = new Employee("INVALIDID", "Jack Doe", "invalid@invalid.com", "1979-01-01", null);
		employeeService.updateEmployee(employeeToUpdate);
	}

	@Test(expected = DepartmentNotFoundException.class)
	public void testUpdateEmployeeWithInvalidDepartment()
			throws DepartmentNotFoundException, EmployeeNotFoundException {
		Employee employeeToUpdate = initialEmployeeslist.get(0);
		employeeToUpdate.setDepartment(new Department((long) 99, "NONE"));
		employeeService.updateEmployee(employeeToUpdate);
	}

	// Get Employee TESTS

	@Test
	public void testGetEmployeeById() throws EmployeeNotFoundException {
		Employee employee = initialEmployeeslist.get(0);
		Employee employeeFromDB = employeeService.getEmployeeById(employee.getId());
		assertEquals(employeeFromDB.getEmail(), employee.getEmail());
		assertEquals(employeeFromDB.getName(), employee.getName());
		assertEquals(employeeFromDB.getBirthday(), employee.getBirthday());
	}

	@Test(expected = EmployeeNotFoundException.class)
	public void testGetEmployeeByInvalidId() throws EmployeeNotFoundException {
		employeeService.getEmployeeById("INVALID ID");
	}

	@Test
	public void testDeleteEmployeeById() throws EmployeeNotFoundException {
		List<Employee> employeeList = employeeService.getAllEmployees();
		int sizeBeforeDelete = employeeList.size();
		String employeeIdToDelete = employeeList.get(0).getId();

		// delete from DB
		employeeService.deleteEmployeeById(employeeIdToDelete);

		int sizeAfterDelete = employeeService.getAllEmployees().size();

		assertEquals((sizeBeforeDelete - 1), sizeAfterDelete);

	}

	@Test(expected = EmployeeNotFoundException.class)
	public void testDeleteEmployeeByInvalidId() throws EmployeeNotFoundException {
		employeeService.getEmployeeById("INVALID ID");
	}

}