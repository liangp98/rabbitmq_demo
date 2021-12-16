package com.wlp.rabbitmq.five;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

/**
 * 发布订阅模式
 */
public class FanoutConsumer02 {

    public static final String EXCHANGE_NAME = "logs";


    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMQUtils.getChannel();

        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        /**
         * 声明临时队列
         */
        String queueName = channel.queueDeclare().getQueue();

        //绑定交换机和队列
        channel.queueBind(queueName,EXCHANGE_NAME,"");

        DeliverCallback deliverCallback = ( consumerTag,  message)->{
            System.out.println("FanoutConsumer02收到"+ message.getEnvelope().getDeliveryTag() +"消息："+ new String(message.getBody()));
        };

        //消费
        channel.basicConsume(queueName,true,deliverCallback,(consumerTag)->{});


    }



}
