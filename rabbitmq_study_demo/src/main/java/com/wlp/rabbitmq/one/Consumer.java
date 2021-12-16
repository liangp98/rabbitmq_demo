package com.wlp.rabbitmq.one;

import com.rabbitmq.client.*;

/**
 *
 */
public class Consumer {

    public static final String QUEUE_NAME = "test_queue01";



    public static final String HOST = "192.168.88.128";
//    public static final String HOST = "127.0.0.1";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "123456";


    public static void main(String[] args) throws Exception {

        //连接配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername(USERNAME);
        connectionFactory.setPassword(PASSWORD);
        connectionFactory.setHost(HOST);

        //创建连接和信道
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);


        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        CancelCallback cancelCallback = consumer -> {
            System.out.println("消息消费被中断");
        };

        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }


}
