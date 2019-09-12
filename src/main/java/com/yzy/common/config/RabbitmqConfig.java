package com.yzy.common.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title:
 * @description: RabbitmqConfig 配置
 *
 * @package: com.yzy.common.config.RabbitmqConfig.java
 * @author: yzy
 * @date: 2019-09-12 08:30:36
 * @version: v1.0
 */

@Configuration
public class RabbitmqConfig {

    /**
     * @title:
     * @description: 建立绑定关系
     *
     * @param: []
     * @return: org.springframework.amqp.core.Binding
     */
    @Bean
    public Binding loginBinding() {
        return BindingBuilder.bind(loginQueue()).to(loginExchange()).with("login_binding");
    }

    /**
     * @title:
     * @description: 创建添加通知队列
     *
     * @param: []
     * @return: org.springframework.amqp.core.Queue
     */
    @Bean
    public Queue loginQueue() {
        return new Queue("login");
    }

    /**
     * @title:
     * @description: 创建直连交换器
     *
     * @param: []
     * @return: org.springframework.amqp.core.DirectExchange
     */
    @Bean
    public DirectExchange loginExchange() {
        return new DirectExchange("login_exchange", true, false);
    }

	/**
	 * @title:
	 * @description: 创建topic交换器
	 *
	 * @param: []
	 * @return: org.springframework.amqp.core.TopicExchange
	 */
    @Bean
	public TopicExchange loginTopicExchange(){
    	return new TopicExchange("login_topic_exchange", true, false);
    }

	/**
	 * @title:
	 * @description: 创建fanout交换器
	 *
	 * @param: []
	 * @return: org.springframework.amqp.core.FanoutExchange
	 */
	@Bean
	public FanoutExchange loginFanoutExchange(){
		return new FanoutExchange("login_fanout_exchange", true, false);
	}
}
