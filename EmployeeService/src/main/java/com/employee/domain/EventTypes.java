package com.employee.domain;

import com.employee.messaging.EventMessageProducer;
/**
 * This enum indicates possible values for event types mentioned in {@link Event}. 
 * </br>
 * Whenever a change occurs on an employee the related event is put into an instance of this object and sent to {@link EventMessageProducer}. 
 * @author Jalal
 * @since 20190324
 * @version 1.0
 */
public enum EventTypes{
	CREATED,
	UPDATED,
	DELETED
}
