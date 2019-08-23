package com.yzy.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
     * 创建交换器
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange loginExchange() {
        return new DirectExchange("login_exchange", true, false);
    }

}
