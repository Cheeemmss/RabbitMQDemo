package com.wl.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.wl.utils.RabbitMQUtils;

import java.util.Scanner;

public class DirectProducer {

    private static final String DIRECT_EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(DIRECT_EXCHANGE_NAME,"queue2_key2",null,message.getBytes());
            System.out.println(message+"发送成功");
        }
    }

}
