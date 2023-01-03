package com.wl.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PublishConfirmConfig {

    public static final String CONFIRM_EXCHANGE = "confirm_exchange";

    public static final String CONFIRM_QUEUE = "confirm_queue";

    public static final String ROUTING_KEY = "routing_key";

    public static final String BACKUP_EXCHANGE = "backup_exchange";

    public static final String BACKUP_QUEUE = "backup_queue";

    public static final String WARNING_QUEUE = "warning_queue";

    @Bean
    public DirectExchange confirmExchange(){
        //设置confirm交换机的备份交换机
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE)
                .durable(true).withArgument("alternate-exchange",BACKUP_EXCHANGE).build();
    }

    @Bean
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    @Bean
    public Binding bindingConfirmQueue(DirectExchange confirmExchange,
                                       Queue confirmQueue){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(ROUTING_KEY);
    }

    @Bean
    public FanoutExchange backupExchange(){
        return ExchangeBuilder.fanoutExchange(BACKUP_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }

    @Bean
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

    @Bean
    public Binding bindingBackupExchangeAndBQ(FanoutExchange backupExchange,
                                         Queue backupQueue
                                         ){
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    @Bean
    public Binding bindingBackupExchangeAndWQ(FanoutExchange backupExchange,
                                         Queue warningQueue
    ){
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
