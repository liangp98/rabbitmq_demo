package com.wlp.rabbitmq.nine;

import com.rabbitmq.client.*;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 优先级队列生产者
 */
public class PriorityConsumer {

    public static final String PRIORITY_EXCHANGE = "priority_exchange";
    public static final String PRIORITY_QUEUE = "priority_queue";
    public static final String PRIORITY_ROUTING_KEY = "priority_routing_key";
    public static final Integer PRIORITY_NUM = 10;

    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMQUtils.getChannel();

        channel.exchangeDeclare(PRIORITY_EXCHANGE, BuiltinExchangeType.DIRECT);
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority",PRIORITY_NUM);

        channel.queueDeclare(PRIORITY_QUEUE,false,false,false,arguments);

        channel.queueBind(PRIORITY_QUEUE,PRIORITY_EXCHANGE,PRIORITY_ROUTING_KEY);

        DeliverCallback deliverCallback = (consumerTag, message)->{
            String msg = new String(message.getBody());
            System.out.println("消息内容：" + msg);
        };
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println("取消消费动作......");
        };

        //接收
        channel.basicConsume(PRIORITY_QUEUE,true,deliverCallback,cancelCallback);


    }

}
