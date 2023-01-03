package com.wl.RabbitMQDemo2;

import com.rabbitmq.client.Channel;
import com.wl.utils.RabbitMQUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Task01 {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String message="hello world";
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String msg = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            System.out.println("发送消息："+msg+" 完成");
        }
    }
}
