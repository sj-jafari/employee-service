package com.employee.domain;

import java.io.Serializable;

import com.employee.messaging.EventMessageProducer;

import lombok.Data;

/**
 * Keeps information of an Event related to an employee. 
 * Whenever a change occurs on an employee, the related event is put into an
 * instance of this object and sent to {@link EventMessageProducer}.
 * 
 * @author Jalal
 * @since 20190324
 * @version 1.0
 */
@Data
public class EmployeeEvent extends Event {
	private Employee employee;

	public EmployeeEvent(String entityName, EventTypes eventType, String timestamp, Employee employee) {
		super(entityName, eventType, timestamp);
		this.employee = employee;
	}

}
