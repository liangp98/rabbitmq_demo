package com.wlp.rabbitmq.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

import java.util.concurrent.TimeUnit;

/**
 *  手动ACK确认
 *
 *  测试消费者获取消息后，出现异常，消息未正常消费；该消息重新入队列，分配给其他可以消费的消费者
 */
public class Work01 {

    public static final String ACK_QUEUE = "ack_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("work01开始消费消息，休眠时间1s");

        //预取值：分配给该信道的消息的最多为消费数量,若是超过此值，则分配给其他消费者
        channel.basicQos(2);

        DeliverCallback deliverCallback = (consumerTag,message)->{
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println("work01消费消息：" + new String(message.getBody()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //手动确认
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println("work01取消消费动作......");
        };

        channel.basicConsume(ACK_QUEUE,false,deliverCallback,cancelCallback);

    }

}
