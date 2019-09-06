package com.yzy.common.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * describe:
 *
 * @author yzy
 * @date 2019/07/04
 */
@Configuration
public class RabbitmqConfig {

    /**
     * 建立绑定关系
     *
     * @return Binding
     */
    @Bean
    public Binding loginBinding() {
        return BindingBuilder.bind(loginQueue()).to(loginExchange()).with("login_binding");
    }

    /**
     * 创建添加通知队列
     *
     * @return Queue
     */
    @Bean
    public Queue loginQueue() {
        return new Queue("login");
    }

    /**
     * 创建直连交换器
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange loginExchange() {
        return new DirectExchange("login_exchange", true, false);
    }

	/**
	 * 创建topic交换器
	 *
	 * @return TopicExchange
	 */
    @Bean
	public TopicExchange loginTopicExchange(){
    	return new TopicExchange("login_topic_exchange", true, false);
    }

	/**
	 * 创建fanout交换器
	 *
	 * @return FanoutExchange
	 */
	@Bean
	public FanoutExchange loginFanoutExchange(){
		return new FanoutExchange("login_fanout_exchange", true, false);
	}
}
