package com.wl.publishconfirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.wl.config.PublishConfirmConfig.CONFIRM_QUEUE;
import static com.wl.config.PublishConfirmConfig.WARNING_QUEUE;

@Component
@Slf4j
public class Consumer {


    @RabbitListener(queues = CONFIRM_QUEUE)
    public void receiveMessage(Message message){
        String msg = new String(message.getBody());
        log.info("消费者收到了消息{}",msg);
    }

    @RabbitListener(queues = WARNING_QUEUE)
    public void receiveWarningMessage(Message message){
        String msg = new String(message.getBody());
        log.warn("warningQueue收到了未被正确路由的消息{}",msg);
    }
}
