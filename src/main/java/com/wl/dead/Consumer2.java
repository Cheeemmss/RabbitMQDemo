package com.wl.dead;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wl.utils.RabbitMQUtils;

import java.util.HashMap;

public class Consumer2 {

    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        DeliverCallback ackCallback = (consumerTag, message) ->{
            String msg= new String(message.getBody());
            System.out.println("Consumer2收到了消息: "+msg);
        };

        CancelCallback nackCallback = (consumerTag) -> {
            System.out.println("Consumer2接收消息中断");
        };


        channel.basicConsume(DEAD_QUEUE,true,ackCallback,nackCallback);


    }
}
