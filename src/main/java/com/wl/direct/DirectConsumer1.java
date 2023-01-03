package com.wl.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wl.utils.RabbitMQUtils;

public class DirectConsumer1 {

    private static final String DIRECT_EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        //声明交换机(fanout)
        channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //声明队列queue1
        channel.queueDeclare("queue1",false,false,false,null);

        //绑定队列和交换机(绑定1个routingKey)
        channel.queueBind("queue1",DIRECT_EXCHANGE_NAME,"queue1_key1");


        DeliverCallback ackCallback = (consumerTag, message) ->{
            String msg= new String(message.getBody());
            System.out.println("DirectConsumer1收到了消息: "+msg);
        };

        CancelCallback nackCallback = (consumerTag) ->{
            System.out.println("DirectConsumer1接收消息中断");
        };


        channel.basicConsume("queue1",true,ackCallback,nackCallback);
    }
}
