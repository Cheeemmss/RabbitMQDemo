package com.wl.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;


@Configuration
public class DelayQueueConfig {

    private static final String DELAY_EXCHANGE_NAME = "delay_exchange";

    private static final String DELAY_QUEUE = "delay_queue";

    private static final String DELAY_ROUTING_KEY = "delay_routingKey";

    //自定义交换机
    @Bean
    public CustomExchange delayExchange(){
        HashMap<String,Object> args = new HashMap<>();
        //设置交换机类型(与下面的x-delayed-message并不冲突,一直指定延迟,一个指定消息分发模式)
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_EXCHANGE_NAME,"x-delayed-message",true,false,args);
    }

    //定义延迟队列
    @Bean
    public Queue delayQueue(){
        return new Queue(DELAY_QUEUE);
    }

    //进行绑定
    @Bean
    public Binding bindingDelayExchangeAndQueue(CustomExchange delayExchange,
                                                Queue delayQueue){
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAY_ROUTING_KEY).noargs();
    }

}
