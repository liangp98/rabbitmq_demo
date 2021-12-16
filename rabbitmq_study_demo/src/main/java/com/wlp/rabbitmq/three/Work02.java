package com.wlp.rabbitmq.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

import java.util.concurrent.TimeUnit;


/**
 *  手动ACK确认
 *
 *  测试消费者获取消息后，出现异常，消息未正常消费；该消息重新入队列，分配给其他可以消费的消费者
 */
public class Work02 {

    public static final String ACK_QUEUE = "ack_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("work02开始消费消息，休眠时间10s");
        channel.basicQos(3);
        DeliverCallback deliverCallback = (consumerTag,message)->{
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("work02消费消息：" + new String(message.getBody()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //手动确认
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println("work02取消消费动作......");
        };

        channel.basicConsume(ACK_QUEUE,false,deliverCallback,cancelCallback);

    }

}
