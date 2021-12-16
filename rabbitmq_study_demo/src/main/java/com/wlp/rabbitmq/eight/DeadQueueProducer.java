package com.wlp.rabbitmq.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.AMQBasicProperties;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * 死信队列 生产者
 * <p>
 * 消息进入死信队列的情况：
 * 1、TTL到期
 * 2、消息被拒绝
 * 3、队列达到最大长度
 */
public class DeadQueueProducer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static final String NORMAL_QUEUE = "normal_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);

        //设置死信时间TTL(单位：ms)
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                    .builder().expiration("10000").build()
        ;

        System.out.println("生产者生产消息中.......");
        for (int i = 0; i < 10; i++) {
//            TimeUnit.SECONDS.sleep(1);
            String message = "MSG - " + i;
//            channel.basicPublish(NORMAL_EXCHANGE, "normal", basicProperties, message.getBytes("UTF-8"));
            channel.basicPublish(NORMAL_EXCHANGE, "normal", null, message.getBytes("UTF-8"));
            System.out.println("生产者推送消息：" + message);
        }


//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()){
//
//            String message = scanner.next();
//            channel.basicPublish(NORMAL_EXCHANGE,"normal",null,message.getBytes("UTF-8"));
//            System.out.println("生产者推送消息：" + message);
//        }

    }

}
