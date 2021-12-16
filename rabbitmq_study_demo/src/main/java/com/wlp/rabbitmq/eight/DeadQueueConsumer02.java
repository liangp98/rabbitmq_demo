package com.wlp.rabbitmq.eight;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 死信队列 消费者
 *
 * 消息进入死信队列的情况：
 * 1、TTL到期
 * 2、消息被拒绝
 * 3、队列达到最大长度
 *
 *
 */
public class DeadQueueConsumer02 {

    //普通交换机和死信交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";

    //普通队列和死信队列
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();

        //声明普通交换机和死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明普通队列,并配置死信参数(ttl、死信交换机、死信routingKey)
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //绑定交换机和队列
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"dead");



        DeliverCallback deliverCallback = ( consumerTag,  message)->{
            System.out.println("死信消费DeadQueueConsumer02接收的消息是：" + new String(message.getBody(),"UTF-8"));
        };

        CancelCallback cancelCallback = (consumerTa)->{
            System.out.println("DeadQueueConsumer02取消接收消息" );
        };

        //消费
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,cancelCallback);

    }


}
