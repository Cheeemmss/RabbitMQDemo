package com.wl.RabbitMQDemo3;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wl.utils.RabbitMQUtils;

public class Worker3 {

    private final static String TASK_QUEUE1 = "task_queue1";

    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMQUtils.getChannel();

        //推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (consumerTag, delivery)->{
            String message= new String(delivery.getBody());
            System.out.println("c2接收到了消息: "+message+"正在处理...");
            try {
                //模拟业务逻辑处理了30s
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //设置不批量应答
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            System.out.println("业务逻辑处理完成,完成手动应答");
        };

        //取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println(consumerTag + "消息消费被中断");
        };

        //设置是否自动应答为false
        boolean autoAck = false;
        System.out.println("c2等待接收消息...");
        channel.basicQos(5);
        channel.basicConsume(TASK_QUEUE1,autoAck,deliverCallback,cancelCallback);
    }
}
