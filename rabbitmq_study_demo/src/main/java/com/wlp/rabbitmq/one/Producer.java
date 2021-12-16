package com.wlp.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 简单模式
 */
public class Producer {

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

//        //配置消费者
//        channel.basicConsume(QUEUE_NAME,false,null);

        //发送消息
        String[] message = {"你好啊","我是吴怼怼","很高兴认识你"};
        for (int i=0; i< message.length;i++){
            channel.basicPublish("",QUEUE_NAME,null,message[i].getBytes());
        }

        System.out.println("消息发送成功.............");


    }


}
