package com.employee.domain;

import com.employee.messaging.EventMessageProducer;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Keeps general information of an Event. The event types can be
 * found in {@link EventTypes}. </br>
 * Whenever a change occurs, the related event is put into an
 * instance of this object and sent to {@link EventMessageProducer}.
 * 
 * @author Jalal
 * @since 20190324
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class Event {
	private String entityName;
	private EventTypes eventType;
	private String timestamp;
}
