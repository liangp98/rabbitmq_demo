package com.wlp.rabbitmq.consumer;

import cn.hutool.core.date.DateUtil;
import com.rabbitmq.client.Channel;
import com.wlp.rabbitmq.config.DelayedQueueConfig;
import com.wlp.rabbitmq.config.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *
 *  基于插件的延时队列
 *
 */
@Slf4j
@Component
public class DelayQueueConsumer {
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE)
    public void receiveMsg(Message message, Channel channel){

        String msg = new String(message.getBody());

        log.info("时间:{}, 延迟队列接收到的消息是：{}", DateUtil.now(),msg);

    }


}
