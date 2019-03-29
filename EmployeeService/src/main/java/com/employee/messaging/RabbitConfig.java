package com.employee.messaging;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * The configuration class for RabbitMQ message broker. Connection info are
 * introduced in application.properties file.
 * 
 * @author Jalal
 * @since 20190324
 */
@Configuration
public class RabbitConfig {
	// @Value("${queues.employee.events}")
	public static String QUEUE_EMPLOYEE_EVENTS;// = "employee-events-queue";
	// @Value("${exchanges.employee}")
	public static String EXCHANGE_EMPLOYEES;// = "employee-exchange";
	
	@Autowired
	Environment env;
	
	@PostConstruct
	private void init() {
		QUEUE_EMPLOYEE_EVENTS = env.getProperty("queues.employee.events");
		EXCHANGE_EMPLOYEES = env.getProperty("exchanges.employee");
	}
	/**
	 * Returns a queue for employee events.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return Queue
	 */
	@Bean
	public Queue employeeEventsQueue() {
		return QueueBuilder.durable(QUEUE_EMPLOYEE_EVENTS).build();
	}

	/**
	 * Returns an exchange for employee events.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return Exchange
	 */
	@Bean
	public Exchange employeesExchange() {
		return ExchangeBuilder.topicExchange(EXCHANGE_EMPLOYEES).build();
	}

	/**
	 * Binds the employeeEventsQueue to employeesExchange with the
	 * employeeEventsQueue as routing key.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return Binding
	 */
	@Bean
	public Binding binding(Queue employeeEventsQueue, TopicExchange employeesExchange) {
		return BindingBuilder.bind(employeeEventsQueue).to(employeesExchange).with(QUEUE_EMPLOYEE_EVENTS);
	}
}