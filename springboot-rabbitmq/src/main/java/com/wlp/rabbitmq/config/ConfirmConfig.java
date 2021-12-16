package com.wlp.rabbitmq.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 *  发布确认 --- 高级内容
 *
 */
@Configuration
public class ConfirmConfig {

    //交换机名称
    public static final String CONFIRM_EXCHANGE = "confirm_exchange";
    //队列
    public static final String CONFIRM_QUEUE = "confirm_queue";
    //routingKey
    public static final String CONFIRM_ROUTING_KEY = "confirm_routing_key";



    @Bean
    public DirectExchange confirmExchange(){

        //无备份交换机
//        return new DirectExchange(CONFIRM_EXCHANGE,true,false);

        return ExchangeBuilder
                .directExchange(CONFIRM_EXCHANGE)
                .durable(true)
                .withArgument("alternate-exchange",BACKUP_EXCHANGE)     //设置备份交换机
                .build();
    }

    @Bean
    public Queue confirmQueue(){

        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    @Bean
    public Binding confirmExchangeBindingConfirmQueue(){
        return BindingBuilder.bind(confirmQueue()).to(confirmExchange()).with(CONFIRM_ROUTING_KEY);
    }


    /*************************************************************************************/

    //备份交换交换机
    public static final String BACKUP_EXCHANGE = "backup_exchange";
    //队列
    public static final String BACKUP_QUEUE = "backup_queue";
    public static final String WARNING_QUEUE = "warning_queue";

    @Bean
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE,true,false);
    }

    @Bean
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }
    @Bean
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

    @Bean
    public Binding backupExchangeBindingBackupQueue(){
        return BindingBuilder.bind(backupQueue()).to(backupExchange());
    }
    @Bean
    public Binding backupExchangeBindingWarningQueue(){
        return BindingBuilder.bind(warningQueue()).to(backupExchange());
    }


}
