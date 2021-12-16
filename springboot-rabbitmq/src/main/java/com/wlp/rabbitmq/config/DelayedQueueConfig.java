package com.wlp.rabbitmq.config;

import com.rabbitmq.client.BuiltinExchangeType;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * 插件版延时队列
 */
@Configuration
public class DelayedQueueConfig {


    public static final String DELAYED_EXCHANGE = "delayed.exchange";
    public static final String DELAYED_QUEUE = "delayed.queue";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    //自定义交换机类型
    public static final String DELAYED_EXCHANGE_MESSAGE = "x-delayed-message";
    public static final String DELAYED_EXCHANGE_TYPE = "x-delayed-type";


    @Bean
    public Queue delayedQueue(){

        return QueueBuilder.durable(DELAYED_QUEUE).build();
//        return new Queue(DELAYED_QUEUE);
    }


    @Bean
    public CustomExchange delayedExchange(){

        Map<String, Object> arguments = new HashMap<>();
        arguments.put(DELAYED_EXCHANGE_TYPE, "direct");

        return new CustomExchange(DELAYED_EXCHANGE,DELAYED_EXCHANGE_MESSAGE,true,false ,arguments);

    }


    @Bean
    public Binding delayedQueueBindingDelayedExchange(){

        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with(DELAYED_ROUTING_KEY).noargs();
    }

}
