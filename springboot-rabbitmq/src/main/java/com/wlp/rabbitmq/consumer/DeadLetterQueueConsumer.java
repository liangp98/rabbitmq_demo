package com.wlp.rabbitmq.consumer;

import cn.hutool.core.date.DateUtil;
import com.rabbitmq.client.Channel;
import com.wlp.rabbitmq.config.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *
 *  延时队列
 *
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = TtlQueueConfig.DEAD_LETTER_QUEUE_D)
    public void receiveMsg(Message message, Channel channel){

        String msg = new String(message.getBody());

        log.info("时间:{}, 死信队列接收到的消息是：{}", DateUtil.now(),msg);

    }


}
