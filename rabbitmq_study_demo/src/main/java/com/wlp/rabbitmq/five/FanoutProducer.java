package com.wlp.rabbitmq.five;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

import java.util.Scanner;

/**
 *  fanout 发布订阅模式，广播
 */
public class FanoutProducer {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();

        //绑定交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {

            String message = scanner.next();

            //发消息: 广播模式下，不论routingKey是什么，与交换机绑定队列都收到消息
            channel.basicPublish(EXCHANGE_NAME, "asdf", null, message.getBytes());

        }


    }

}
