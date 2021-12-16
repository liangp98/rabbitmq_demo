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
 *  交换机路由不到队列时候，才会被备份转发到备份交换机上。优先级 备份处理 大于 -> 回退处理
 *
 *                          ------- 备份队列 backup_queue
 *                          |
 *      备份交换机backup ----
 *                          |
 *                          ------- 预警队列 warning_queue -----  WarningQueueConsumer
 *  备份队列 --- 消费者
 *
 */
@Slf4j
@Component
public class WarningQueueConsumer {


    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE)
    public void receiveMsg(Message message, Channel channel){

        String contend = new String(message.getBody());
        log.info("预警消费者 ---> 时间：{}，内容：{}", DateUtil.now(),contend);

    }


}
