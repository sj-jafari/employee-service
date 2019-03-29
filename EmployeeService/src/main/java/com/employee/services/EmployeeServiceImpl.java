package com.employee.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.domain.Department;
import com.employee.domain.Employee;
import com.employee.domain.EmployeeEvent;
import com.employee.domain.EventTypes;
import com.employee.exceptions.DepartmentNotFoundException;
import com.employee.exceptions.EmployeeNotFoundException;
import com.employee.messaging.EventMessageProducer;
import com.employee.repositories.EmployeeRepository;

/**
 * This class provides an implementation for Services related to
 * {@link Employee}
 * 
 * @author Jalal
 * @since 20190324
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private EventMessageProducer eventMessageSender;

	/**
	 * Saves a given {@link Employee} into the DB. It also checks if the given
	 * department for the employee exists on DB. Null values for department are
	 * acceptable. </br>
	 * It also sends the related event to {@link EventMessageProducer}.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return Employee
	 * @throws DepartmentNotFoundException
	 */
	@Override
	public Employee saveEmployee(Employee employee) throws DepartmentNotFoundException {
		// TODO: put these actions in a transaction

		// check the passed department
		Department department;
		// allow nulls
		if (employee.getDepartment() != null) {
			department = departmentService.getDepartmentById(employee.getDepartment().getId());
			employee.setDepartment(department);
		}
		Employee resultEmployee = employeeRepository.save(employee);
		// send the event
		createAndSendEmployeeEvent(resultEmployee, EventTypes.CREATED);

		return resultEmployee;
	}

	/**
	 * Updates a given {@link Employee} into the DB. It also checks if the given
	 * employee and its department exist on DB. Null values for department are
	 * acceptable. </br>
	 * It also sends the related event to {@link EventMessageProducer}.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return Employee
	 * @throws DepartmentNotFoundException, EmployeeNotFoundException
	 */
	@Transactional
	@Override
	public Employee updateEmployee(Employee employee) throws EmployeeNotFoundException, DepartmentNotFoundException {
		// TODO: put these actions in a transaction

		// this will cause EmployeeNotFoundException, if it does not exist on db.
		this.getEmployeeById(employee.getId().toString());

		// check the passed department
		Department department;
		// allow nulls
		if (employee.getDepartment() != null) {
			department = departmentService.getDepartmentById(employee.getDepartment().getId());
			employee.setDepartment(department);
		}
		Employee resultEmployee = employeeRepository.save(employee);
		// send the event
		createAndSendEmployeeEvent(resultEmployee, EventTypes.UPDATED);

		return resultEmployee;
	}

	/**
	 * Returns an {@link Employee} given its UUID. </br>
	 * It also sends the related event to {@link EventMessageProducer}.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @throws EmployeeNotFoundException
	 */
	@Override
	public Employee getEmployeeById(String uuid) throws EmployeeNotFoundException {
		Employee employee = employeeRepository.getById(uuid);
		if (employee == null)
			throw new EmployeeNotFoundException(uuid);
		return employee;
	}

	/**
	 * Returns a list of {@link Employee} objects.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return List<Employee>
	 */
	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> employees = new ArrayList<>();
		employeeRepository.findAll().forEach(employees::add);

		return employees;
	}

	/**
	 * Deletes an {@link Employee} given its UUID. </br>
	 * It also sends the related event to {@link EventMessageProducer}.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @throws EmployeeNotFoundException
	 */
	@Override
	public void deleteEmployeeById(String uuid) throws EmployeeNotFoundException {
		// TODO put it in a transaction

		// this will cause EmployeeNotFoundException, if it does not exist on db.
		Employee resultEmployee = this.getEmployeeById(uuid);
		employeeRepository.deleteById(uuid);
		// send the event
		createAndSendEmployeeEvent(resultEmployee, EventTypes.DELETED);
	}

	/**
	 * A private method to take care of event sending mechanics.
	 * 
	 * @author Jalal
	 * @since 20190324
	 */
	private void createAndSendEmployeeEvent(Employee employee, EventTypes eventType) {
		EmployeeEvent event = new EmployeeEvent(Employee.class.getSimpleName(), eventType,
				new Timestamp(System.currentTimeMillis()).toString(), employee);
		eventMessageSender.sendEmployeeEvent(event);
	}
}
