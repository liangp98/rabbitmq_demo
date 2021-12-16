package com.wlp.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 *
 *  1、交换机收不到消息，消息回调ConfirmCallback
 *  2、交换机的消息路由不到队列，消息回调ReturnCallback
 *
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // @PostConstruct是一种JSR-250的规范，当bean创建完成的时候，会后置执行@PostConstruct修饰的方法
    @PostConstruct
    public void initConfirmCallback(){
        rabbitTemplate.setConfirmCallback(this);
    }

    @PostConstruct
    public void initReturnCallback(){
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     *
     * @param correlationData 含消息的ID及相关内容
     * @param ack   交换机收到消息位true，否则为false
     * @param cause {@param ack }为true时，该参数为null, 反之有为false的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        String id = Objects.isNull(correlationData)?"":correlationData.getId();

        String message = Objects.isNull(correlationData)?null:new String(correlationData.getReturnedMessage().getBody());
        if (ack){
            log.info("成功接收到ID为{}的消息, 内容：{}",id,message);
        }else {
            log.info("未接收到ID为{}的消息, 原因：{}",id,cause);
        }

    }

    /**
     *  消息不可达时，回退到生产者
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息被回退啦！内容消息：{}，被交换机：{}退回，退回原因：{}，路由Key:{}",
                new String(message.getBody()),exchange,replyText,routingKey);
    }
}
