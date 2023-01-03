package com.wl.RabbitMQDemo4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.wl.utils.RabbitMQUtils;

public class Task03 {

    public static final String TASK_QUEUE2 = "task_queue2";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        channel.queueDeclare(TASK_QUEUE2,true,false,false,null);
        //开启确认发布
        channel.confirmSelect();

        long begin = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String message = i + "";
            channel.basicPublish("",TASK_QUEUE2, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());

            ////服务端返回 false 或超时时间内未返回，生产者可以消息重发
            boolean confirms = channel.waitForConfirms();
            if(confirms){
                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();

        System.out.println("单个确认发布发送1000条消息用时用时:"+(end-begin));
        //单个确认发布发送1000条消息用时用时:1128
    }
}
