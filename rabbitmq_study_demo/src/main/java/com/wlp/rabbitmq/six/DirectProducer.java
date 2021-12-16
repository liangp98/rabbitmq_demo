package com.wlp.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

import java.util.Scanner;

/**
 *  direct 路由模式
 *
 */
public class DirectProducer {
    public static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();

        //绑定交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {

            //发消息
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "consumer01", null, message.getBytes());

        }


    }

}
