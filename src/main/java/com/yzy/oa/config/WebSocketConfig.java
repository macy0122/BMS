package com.yzy.oa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @title:
 * @description: 通过EnableWebSocketMessageBroker 开启使用STOMP协议来传输基于代理(message broker)的消息,
 * 此时浏览器支持使用@MessageMapping 就像支持@RequestMapping一样。
 * @package: com.yzy.oa.config.WebSocketConfig.java
 * @author: yzy
 * @date: 2019-09-17 13:53:29
 * @version: v1.0
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	/**
	 * @title:
	 * @description: endPoint 注册协议节点,并映射指定的URl
	 * @param: [registry]
	 * @return: void
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//注册一个名字为"endpointChat" 的endpoint,并指定 SockJS协议。   点对点-用
		registry.addEndpoint("/endpointChat").withSockJS();
	}

	/**
	 * @title:
	 * @description: 配置消息代理(message broker)
	 * @param: [registry]
	 * @return: void
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//点对点式增加一个/queue 消息代理
        //广播式应配置一个/topic 消息代理
		registry.enableSimpleBroker("/queue", "/topic");
	}

}
