package com.wl.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wl.utils.RabbitMQUtils;

public class DirectConsumer2 {

    private static final String DIRECT_EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        //声明交换机(fanout)
        channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //声明队列queue2
        channel.queueDeclare("queue2",false,false,false,null);

        //绑定队列和交换机(绑定2个routingKey)
        channel.queueBind("queue2",DIRECT_EXCHANGE_NAME,"queue2_key1");
        channel.queueBind("queue2",DIRECT_EXCHANGE_NAME,"queue2_key2");


        DeliverCallback ackCallback = (consumerTag, message) ->{
            String msg= new String(message.getBody());
            System.out.println("DirectConsumer2收到了消息: "+msg);
        };

        CancelCallback nackCallback = (consumerTag) -> {
            System.out.println("DirectConsumer2接收消息中断");
        };


        channel.basicConsume("queue2",true,ackCallback,nackCallback);
    }

}
