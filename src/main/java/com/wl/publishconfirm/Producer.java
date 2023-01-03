package com.wl.publishconfirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

import static com.wl.config.PublishConfirmConfig.CONFIRM_EXCHANGE;
import static com.wl.config.PublishConfirmConfig.ROUTING_KEY;

@Controller
@RequestMapping("/publish")
@Slf4j
public class Producer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/{msg}")
    public void sendMessage(@PathVariable("msg") String msg){
        //模拟发送的这条消息的id为1
        CorrelationData correlationData = new CorrelationData("1");
        //可通过修改交换机名字来使得消息无法投递到交换机,通过修改routingKey使得消息正确到达交换机后无法正确进行路由
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE,ROUTING_KEY+1,msg.getBytes(),correlationData);
        log.info("生产者发送了消息{}",msg);
    }


}
