package com.wl.RabbitMQDemo4;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wl.utils.RabbitMQUtils;


@Deprecated
public class Worker4 {
    private final static String TASK_QUEUE2 = "task_queue2";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();

        //推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (consumerTag, delivery)->{
            String message= new String(delivery.getBody());
            System.out.println(message);
            //设置不批量应答
            //delivery.getEnvelope().getDeliveryTag()是该条消息对应的一个Tag
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            System.out.println("业务逻辑处理完成,完成手动应答");
        };

        //取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println(consumerTag + "消息消费被中断");
        };

        //设置是否自动应答为false
        boolean autoAck = false;
        channel.basicQos(2);
        channel.basicConsume(TASK_QUEUE2,autoAck,deliverCallback,cancelCallback);
    }
}
