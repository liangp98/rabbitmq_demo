package com.wlp.rabbitmq.two;

import com.rabbitmq.client.*;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

public class Worker01 {

    public static final String QUEUE_NAME = "test_queue01";


    public static void main(String[] args)throws Exception {

        Channel channel = RabbitMQUtils.getChannel();

        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("出错了....");
        };
        System.out.println("C1线程等待消费中........");
        //消费
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);


    }


}
