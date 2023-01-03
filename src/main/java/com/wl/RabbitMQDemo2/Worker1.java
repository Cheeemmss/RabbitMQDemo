package com.wl.RabbitMQDemo2;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wl.utils.RabbitMQUtils;

public class Worker1 {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();

        //推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (consumerTag, delivery)->{
            String message= new String(delivery.getBody());
            System.out.println(message);
        };

        //取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println(consumerTag + "消息消费被中断");
        };

        System.out.println("c2等待接收消息");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
