package com.wlp.rabbitmq.consumer;

import cn.hutool.core.date.DateUtil;
import com.rabbitmq.client.Channel;
import com.wlp.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 *
 *  发布确认高级 --- 消费者
 *
 */
@Slf4j
@Component
public class ConfirmQueueConsumer {


    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE)
    public void receiveMsg(Message message, Channel channel){

        String contend = new String(message.getBody());
        log.info("时间：{}，内容：{}", DateUtil.now(),contend);

    }


}
