package com.wl.fanoutexchange;

import com.rabbitmq.client.*;
import com.wl.utils.RabbitMQUtils;

public class FanoutConsumer1 {

    private static final String FANOUT_EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        //声明交换机(fanout)
        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        //声明临时队列
        String queue = channel.queueDeclare().getQueue();

        //绑定队列和交换机 因为是fanout(扇出交换机)routingKey不用指定
        channel.queueBind(queue,FANOUT_EXCHANGE_NAME,"");


        DeliverCallback ackCallback = (consumerTag,message) ->{
            String msg= new String(message.getBody());
            System.out.println("FanoutConsumer1收到了消息: "+msg);
        };

        CancelCallback nackCallback = (consumerTag) ->{
            System.out.println("FanoutConsumer1接收消息中断");
        };


        channel.basicConsume(queue,true,ackCallback,nackCallback);
    }
}
