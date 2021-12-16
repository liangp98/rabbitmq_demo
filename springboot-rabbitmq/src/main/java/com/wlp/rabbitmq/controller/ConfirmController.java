package com.wlp.rabbitmq.controller;

import cn.hutool.core.date.DateUtil;
import com.wlp.rabbitmq.config.ConfirmConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@Api(tags = "发布确认高级 - 测试")
@RestController
@RequestMapping("/confirm")
public class ConfirmController {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @ApiOperation("生产者 - (发布确认高级)发送消息")
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable("message") String message) {
        log.info("时间:{},内容：{}", DateUtil.now(), message);


        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        correlationData.setReturnedMessage(new Message(message.getBytes()));


        String contend = "消息来自队列【"+message+"】";
        //方式一
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE, ConfirmConfig.CONFIRM_ROUTING_KEY, contend,correlationData);


    }
    @ApiOperation("生产者 - (消息无法到达交换机)发送消息")
    @GetMapping("/sendPublishMsg/{message}")
    public void sendPublishMsg(@PathVariable("message") String message) {
        log.info("时间:{},内容：{}", DateUtil.now(), message);


        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        correlationData.setReturnedMessage(new Message(message.getBytes()));


        String contend = "消息来自队列【"+message+"】";
        //方式一
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE+"123", ConfirmConfig.CONFIRM_ROUTING_KEY, contend,correlationData);


    }

    @ApiOperation("生产者 - (路由不到队列)发送消息")
    @GetMapping("/sendReturnMsg/{message}")
    public void sendReturnMsg(@PathVariable("message") String message) {
        log.info("时间:{},内容：{}", DateUtil.now(), message);


        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        correlationData.setReturnedMessage(new Message(message.getBytes()));


        String contend = "消息来自队列【"+message+"】";
        //方式一
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE, ConfirmConfig.CONFIRM_ROUTING_KEY + "456", contend,correlationData);

    }

}
