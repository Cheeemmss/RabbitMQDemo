package com.wl.dead;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.wl.utils.RabbitMQUtils;

public class Producer {

    private static final String NORMAL_EXCHANGE_NAME = "normal_exchange";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //设置消息消息过期时间10s
//        AMQP.BasicProperties properties =
//                new AMQP.BasicProperties()
//                .builder().expiration("10000").build();
        for (int i = 0; i < 10; i++) {
            String message = "msg" + i;
            channel.basicPublish(NORMAL_EXCHANGE_NAME,"zhangsan",null,message.getBytes());
        }
    }
}
