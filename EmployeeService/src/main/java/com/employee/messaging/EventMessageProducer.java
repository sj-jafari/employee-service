package com.employee.messaging;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.domain.EmployeeEvent;
/**
 * This class is used for producing event messages and putting them on the rabbitmq broker. 
 * 
 * @author Jalal
 * @since 20190324
 */ 
@Service
public class EventMessageProducer {
    private final RabbitTemplate rabbitTemplate;
 
    /**
     * Takes care of initialization actions after object construction. 
     * 
     * @author Jalal
     * @since 20190324
     */
    @PostConstruct
    private void init() {
    	// we want our messages to be sent in JSON. 
    	rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }
    
    /**
     * Constructor for this class.
     * 
     * @author Jalal
     * @since 20190324
     */
    @Autowired
    public EventMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
 
    /**
     * This method sends an employee event to the broker.
     * 
     * @author Jalal
     * @since 20190324
     */
    public void sendEmployeeEvent(EmployeeEvent event) {
        this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_EMPLOYEE_EVENTS, event);
    }
}