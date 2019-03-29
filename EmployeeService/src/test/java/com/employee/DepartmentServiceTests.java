package com.employee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.employee.domain.Department;
import com.employee.exceptions.DepartmentNotFoundException;
import com.employee.services.DepartmentService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { EmployeeServiceApplication.class })
//@ActiveProfiles("test")
public class DepartmentServiceTests {

	@Autowired
	private DepartmentService departmentService;
	private List<Department> initialDepartmentslist;

	@Before
	public void init() {
		// init departments
		initialDepartmentslist = departmentService.getAllDepartments();
	}

	@Test
	public void testInitialDataExistence() {
		assertNotNull(initialDepartmentslist);
		assertNotEquals(0, initialDepartmentslist.size());
	}

	// ----- SAVE TESTS
	@Test
	public void testSaveDepartment() {
		int sizeBeforeInsert = departmentService.getAllDepartments().size();
		Department department = new Department(null,"Management");

		departmentService.saveDepartment(department);
		int sizeAfterInsert = departmentService.getAllDepartments().size();

		assertEquals(sizeBeforeInsert + 1, sizeAfterInsert);
	}

	//TODO non unique name
	
	// ----- UPDATE TESTS
	@Test
	public void testUpdateDepartment() {
		Department departmentToUpdate = initialDepartmentslist.get(0);

		departmentToUpdate.setName("Test Department");

		Department departmentFromDB = departmentService.updateDepartment(departmentToUpdate);

		assertNotNull(departmentFromDB);
		assertEquals(departmentToUpdate.getId(), departmentFromDB.getId());
		assertEquals(departmentToUpdate.getName(), departmentFromDB.getName());

	}

	@Test
	public void testGetDepartmentById() throws DepartmentNotFoundException {
		Department department = initialDepartmentslist.get(0);
		Department departmentFromDB = departmentService.getDepartmentById(department.getId());

		assertNotNull(departmentFromDB);
		assertEquals(department.getId(), departmentFromDB.getId());
		assertEquals(department.getName(), departmentFromDB.getName());
	}

}