package com.wlp.rabbitmq.controller;

import cn.hutool.core.date.DateUtil;
import com.wlp.rabbitmq.config.DelayedQueueConfig;
import com.wlp.rabbitmq.config.TtlQueueConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *  延时队列
 *
 */

@Api(tags = "延迟消息发送API")
@RestController
@RequestMapping("ttl")
@Slf4j
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ApiOperation("生产者 - 发送消息")
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable("message") String message) {
        log.info("时间:{}, 内容：{}", DateUtil.now(), message);


        //方式一
        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE, TtlQueueConfig.EXCHANGE_ROUTING_KEY_A, "消息来自TTL为10s的队列【{}】" + message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE, TtlQueueConfig.EXCHANGE_ROUTING_KEY_B, "消息来自TTL为40s的队列【{}】" + message);


//        //方式二
//        String msg = "消息来自TTL为10s的队列【"+message+"】";
//        rabbitTemplate.send(X_EXCHANGE,EXCHANGE_ROUTING_KEY_A,new Message(msg.getBytes()));
//        rabbitTemplate.send(X_EXCHANGE,EXCHANGE_ROUTING_KEY_B,new Message(msg.getBytes()));

    }


    /**
     *  会存在bug
     *  例：
     *     时间:2021-12-15 21:08:11, TTL为：10000ms, 内容：hello
     *     时间:2021-12-15 21:08:12, TTL为：5000ms, 内容：world
     *     时间:2021-12-15 21:08:21, 死信队列接收到的消息是：消息来自TTL为10000ms的队列【hello】
     *     时间:2021-12-15 21:08:21, 死信队列接收到的消息是：消息来自TTL为5000ms的队列【world】
     *
     *
     * hello消息是10s后过期，world消息是5s后过期 ；理论上是world消息先收到，hello消息后收到，实际则相反；
     *
     * 解释：多条消息时候，rabbitmq只会检查第一条消息是否过期；如果过期则丢到死信队列中去；但若是第一个消息延时时间很长，第二
     *      个消息延时很短，第二个消息并不会优先得到执行。
     *
     * 队列先进先出，hello先进队列，world后进，即使消息过期了，也是hello先出队列，world后出队列
     *
     *
     * @param ttlTime
     * @param message
     */
    @ApiOperation("生产者 - (自定义TTL)发送消息")
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsgExpire(@PathVariable("ttlTime") Integer ttlTime, @PathVariable("message") String message) {
        log.info("时间:{}, TTL为：{}ms, 内容：{}", DateUtil.now(), ttlTime, message);

        String contend = "消息来自TTL为" + ttlTime + "ms的队列【"+message+"】";
        //方式一
        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE, TtlQueueConfig.EXCHANGE_ROUTING_KEY_C, contend, msg -> {
            //设置发送消息时长
            msg.getMessageProperties().setExpiration(String.valueOf(ttlTime));

            return msg;
        });


    }


    @ApiOperation("生产者 - (自定义延迟时间)发送消息")
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendDelayMsg(@PathVariable("delayTime") Integer delayTime, @PathVariable("message") String message) {
        log.info("时间:{}, delayTime为：{}ms, 内容：{}", DateUtil.now(), delayTime, message);

        String contend = "消息来自延迟时间为" + delayTime + "ms的队列【"+message+"】";
        //方式一
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE, DelayedQueueConfig.DELAYED_ROUTING_KEY, contend, msg -> {
            //设置发送消息时长 ms
            msg.getMessageProperties().setDelay(delayTime);

            return msg;
        });


    }

}
