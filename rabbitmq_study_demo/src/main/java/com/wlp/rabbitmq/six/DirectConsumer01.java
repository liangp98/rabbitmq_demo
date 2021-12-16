package com.wlp.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

public class DirectConsumer01 {

    public static final String EXCHANGE_NAME = "direct_exchange";


    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMQUtils.getChannel();

        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        /**
         * 声明临时队列
         */
        String queueName = channel.queueDeclare().getQueue();

        //绑定交换机和队列: 同一队列可有多个routingKey
        channel.queueBind(queueName,EXCHANGE_NAME,"consumer01");
        channel.queueBind(queueName,EXCHANGE_NAME,"consumer01_lisa");

        DeliverCallback deliverCallback = ( consumerTag,  message)->{
            System.out.println("DirectConsumer01收到"+ message.getEnvelope().getDeliveryTag() +"消息："+ new String(message.getBody()));
        };

        //消费
        channel.basicConsume(queueName,true,deliverCallback,(consumerTag)->{});


    }



}
