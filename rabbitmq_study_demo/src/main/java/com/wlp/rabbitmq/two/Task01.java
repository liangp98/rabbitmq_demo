package com.wlp.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

public class Task01 {
    public static final String QUEUE_NAME = "test_queue01";


    public static void main(String[] args)throws Exception {

        Channel channel = RabbitMQUtils.getChannel();

        System.out.println("发送消息中........");
        //生产
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //发送消息
        String[] message = {"1-你好啊","2-我是吴怼怼","3-很高兴认识你","4-我也很高兴认识你"};
        for (int i=0; i< message.length;i++){
            channel.basicPublish("",QUEUE_NAME,null,message[i].getBytes());
        }
    }

}
