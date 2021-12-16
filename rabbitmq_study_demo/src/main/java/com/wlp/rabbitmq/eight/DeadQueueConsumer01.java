package com.wlp.rabbitmq.eight;


import com.rabbitmq.client.*;
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
public class DeadQueueConsumer01 {

    //普通交换机和死信交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";

    //普通队列和死信队列
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();

        //声明普通交换机和死信交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明普通队列,并配置死信参数(ttl、死信交换机、死信routingKey)
        Map<String, Object> arguments = new HashMap<>();
//        arguments.put("x-message-ttl",1000*10);     //ttl 单位毫秒
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);  //死信交换机
        arguments.put("x-dead-letter-routing-key","dead");  //死信routingKey
//        arguments.put("x-max-length",6);  //普通队列的最大长度为6,超出的部分进入死信队列
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //绑定交换机和队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"normal");
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"dead");



        DeliverCallback deliverCallback = ( consumerTag,  message)->{
            String msg = new String(message.getBody(),"UTF-8");
            if ("MSG - 3".equals(msg)){
                System.out.println("MSG - 3".equals(msg));
                channel.basicReject(message.getEnvelope().getDeliveryTag(),  false);
                System.out.println("DeadQueueConsumer01拒绝消费的消息是：" + msg);

            }else {
                System.out.println("DeadQueueConsumer01接收的消息是：" + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }

        };

        CancelCallback cancelCallback = (consumerTa)->{
            System.out.println("DeadQueueConsumer01取消接收消息" );
        };

        //消费
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,cancelCallback);

    }


}
