package com.wlp.rabbitmq.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

import java.util.Scanner;

/**
 *  topics 主题模式
 *
 *  * 单一单词匹配
 *  # 多单词匹配
 *
 *
 */
public class TopicProducer {

    public static final String EXCHANGE_NAME = "topics_exchange";

    public static void main(String[] args)throws Exception{

        Channel channel = RabbitMQUtils.getChannel();

        //绑定交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {

            //发消息
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "world.animal.zoo.tiger", null, message.getBytes());

        }
    }
}
