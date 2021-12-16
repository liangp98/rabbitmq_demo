package com.wlp.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtils {
    public static final String HOST = "192.168.88.128";
    //    public static final String HOST = "127.0.0.1";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "123456";


    public static Channel getChannel() throws Exception{

        //连接配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername(USERNAME);
        connectionFactory.setPassword(PASSWORD);
        connectionFactory.setHost(HOST);

        //创建连接和信道
        Connection connection = connectionFactory.newConnection();
        return connection.createChannel();


    }


}
