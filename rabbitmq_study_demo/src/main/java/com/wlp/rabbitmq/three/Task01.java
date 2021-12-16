package com.wlp.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

import java.util.Scanner;


/**
 *
 *  工作模式
 *
 *  手动ACK确认
 *
 *  测试消费者获取消息后，出现异常，消息未正常消费；该消息重新入队列，分配给其他可以消费的消费者
 */
public class Task01 {

    public static final String ACK_QUEUE = "ack_queue";


    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();

        //非持久化队列
        channel.queueDeclare(ACK_QUEUE, false, false, false, null);


        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.next();
            System.out.println("生产者发送消息：" + message);

            //非持久化消息

            channel.basicPublish("", ACK_QUEUE, null, message.getBytes("UTF-8"));
            //持久化第三个参数：MessageProperties.PERSISTENT_TEXT_PLAIN
//            channel.basicPublish("", ACK_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
        }

    }


}
