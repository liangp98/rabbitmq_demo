package com.wlp.rabbitmq.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *  交换机和队列配置类
 *
 *  具有不同过期时间TTL的队列
 *
 *  A:10S
 *  B:40S
 *  C:0S
 */

@Configuration
public class TtlQueueConfig {

    //普通交换机名
    public static final String X_EXCHANGE = "X";
    //死信交换机名
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";

    //普通队列名
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    //死信队列名
    public static final String DEAD_LETTER_QUEUE_D = "QD";

    //普通交换机routingKey
    public static final String EXCHANGE_ROUTING_KEY_A = "XA";
    public static final String EXCHANGE_ROUTING_KEY_B = "XB";
    public static final String EXCHANGE_ROUTING_KEY_C = "XC";
    //死信交换机routingKey
    public static final String DEAD_LETTER_EXCHANGE_ROUTING_KEY = "YD";

    //TTL: 10s  40s
    public static final Integer SHORT_TTL = 1000*10;
    public static final Integer LONG_TTL = 1000*40;



    //声明direct交换机
    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //声明队列
    @Bean("queueA")
    public Queue queueA(){
        //配置关联死信队列信息
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key",DEAD_LETTER_EXCHANGE_ROUTING_KEY);
        arguments.put("x-message-ttl",SHORT_TTL);

        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    @Bean("queueB")
    public Queue queueB(){
        //配置关联死信队列信息
        Map<String, Object> arguments = new HashMap<>(16);
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key",DEAD_LETTER_EXCHANGE_ROUTING_KEY);
        arguments.put("x-message-ttl",LONG_TTL);

        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    @Bean("queueC")
    public Queue queueC(){
        //配置关联死信队列信息
        Map<String, Object> arguments = new HashMap<>(16);
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key",DEAD_LETTER_EXCHANGE_ROUTING_KEY);

        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_D).build();
    }



    //绑定交换机和队列  队列A,B,C--- X交换机； 队列D ---- Y死信交换机
    @Bean
    public Binding queueABindingXExchange(){
        return BindingBuilder.bind(queueA()).to(xExchange()).with(EXCHANGE_ROUTING_KEY_A);
    }
    @Bean
    public Binding queueBBindingXExchange(){
        return BindingBuilder.bind(queueB()).to(xExchange()).with(EXCHANGE_ROUTING_KEY_B);
    }
    @Bean
    public Binding queueCBindingXExchange(){
        return BindingBuilder.bind(queueC()).to(xExchange()).with(EXCHANGE_ROUTING_KEY_C);
    }
    @Bean
    public Binding queueDBindingYExchange(){
        return BindingBuilder.bind(queueD()).to(yExchange()).with(DEAD_LETTER_EXCHANGE_ROUTING_KEY);
    }

}
