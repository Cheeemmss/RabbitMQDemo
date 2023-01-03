package com.wl.RabbitMQDemo4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.wl.utils.RabbitMQUtils;

public class BatchTask {
    public static final String TASK_QUEUE3 = "task_queue3";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        channel.queueDeclare(TASK_QUEUE3,true,false,false,null);
        //开启确认发布
        channel.confirmSelect();

        int batchSize = 100;
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String message = i + "";
            channel.basicPublish("",TASK_QUEUE3, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());

            if(i % batchSize == 0){
                boolean confirms = channel.waitForConfirms();
                if(confirms){
                    System.out.println("100条消息发送成功");
                }
            }
        }
        long end = System.currentTimeMillis();

        System.out.println("批量确认发布发送1000条消息用时用时:"+(end-begin));
        //批量确认发布发送1000条消息用时用时:83

    }
}
