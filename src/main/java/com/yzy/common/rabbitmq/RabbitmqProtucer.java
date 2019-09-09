package com.yzy.common.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitmqProtucer {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 *
	 */
	public void sendMessage(String exchange,String routingKey,Object object){
		rabbitTemplate.convertAndSend(exchange,routingKey,object);
	}
}
