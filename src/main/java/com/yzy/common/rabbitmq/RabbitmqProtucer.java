package com.yzy.common.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @title: 
 * @description: 
 * 
 * @package: com.yzy.common.rabbitmq.RabbitmqProtucer.java
 * @author: yzy
 * @date: 2019-09-23 09:16:20
 * @version: v1.0
 */
@Component
public class RabbitmqProtucer {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * @title: 
	 * @description:
	 *
	 * @param: [exchange, routingKey, object]
	 * @return: void
	 * @version: v1.0
	 */
	public void sendMessage(String exchange,String routingKey,Object object){
		rabbitTemplate.convertAndSend(exchange,routingKey,object);
	}
}
