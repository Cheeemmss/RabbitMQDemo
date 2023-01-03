package com.wl.fanoutexchange;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.wl.utils.RabbitMQUtils;

import java.util.Scanner;

public class FanoutProducer {

    private static final String FANOUT_EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(FANOUT_EXCHANGE_NAME,"",null,message.getBytes());
            System.out.println(message+"发送成功");
        }
    }

}
