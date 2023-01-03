package com.wl.RabbitMQDemo3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.wl.utils.RabbitMQUtils;

import java.util.Scanner;

public class Task02 {

    private final static String TASK_QUEUE1 = "task_queue1";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();
        channel.queueDeclare(TASK_QUEUE1,true,false,false,null);
        channel.confirmSelect();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String msg = scanner.next();
            channel.basicPublish("",TASK_QUEUE1, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());
            System.out.println("发送消息："+msg+" 完成");
        }
//        for (int i = 0; i < 7; i++) {
//            channel.basicPublish("",TASK_QUEUE1, MessageProperties.PERSISTENT_TEXT_PLAIN, Integer.toString(i).getBytes());
//        }
    }
}
