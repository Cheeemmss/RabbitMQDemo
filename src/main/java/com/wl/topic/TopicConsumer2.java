package com.wl.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wl.utils.RabbitMQUtils;

public class TopicConsumer2 {
    private static final String TOPIC_EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        channel.exchangeDeclare(TOPIC_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        channel.queueDeclare("Q2",false,false,false,null);

        channel.queueBind("Q2",TOPIC_EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind("Q2",TOPIC_EXCHANGE_NAME,"lazy.#");

        DeliverCallback ackCallback = (consumerTag, message) ->{
            String msg= new String(message.getBody());
            System.out.println("TopicConsumer2收到了消息: "+msg);
            System.out.println("routingKey: "+ message.getEnvelope().getRoutingKey());
        };

        CancelCallback nackCallback = (consumerTag) ->{
            System.out.println("TopicConsumer2接收消息中断");
        };


        channel.basicConsume("Q2",true,ackCallback,nackCallback);
    }
}
