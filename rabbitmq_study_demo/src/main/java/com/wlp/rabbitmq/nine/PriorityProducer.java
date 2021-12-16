package com.wlp.rabbitmq.nine;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 优先级队列生产者
 */
public class PriorityProducer {

    public static final String PRIORITY_EXCHANGE = "priority_exchange";
    public static final String PRIORITY_QUEUE = "priority_queue";
    public static final String PRIORITY_ROUTING_KEY = "priority_routing_key";
    public static final Integer PRIORITY_NUM = 10;

    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMQUtils.getChannel();

        channel.exchangeDeclare(PRIORITY_EXCHANGE, BuiltinExchangeType.DIRECT);

        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();

        //发送
        for (int i = 0; i < 10; i++) {
            if (5==i){
                String msg = ("MSG - "+i);
                System.out.println(msg);
                channel.basicPublish(PRIORITY_EXCHANGE,PRIORITY_ROUTING_KEY,properties,msg.getBytes());
            }else {
                String msg = ("MSG - "+i);
                System.out.println(msg);
                channel.basicPublish(PRIORITY_EXCHANGE,PRIORITY_ROUTING_KEY,null,msg.getBytes());
            }

        }


    }

}
