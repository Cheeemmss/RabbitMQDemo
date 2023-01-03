package com.wl.dead;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wl.utils.RabbitMQUtils;

import java.util.HashMap;

public class Consumer1 {

    private static final String NORMAL_EXCHANGE_NAME = "normal_exchange";

    private static final String DEAD_EXCHANGE_NAME = "dead_exchange";

    private static final String NORMAL_QUEUE = "normal_queue";

    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        //声明2个交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME,BuiltinExchangeType.DIRECT);


        HashMap<String,Object> arguments = new HashMap<>();

        //设置normal队列要转发的死信交换机 和 死信交换机的routingKey(需要指定不然会报错)
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE_NAME);
        arguments.put("x-dead-letter-routing-key","lisi");
        //设置队列最大长度为6
//        arguments.put("x-max-length",6);

        //声明normal队列和死信队列
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);
        //绑定normal交换机和normal队列  绑定死信交换机和死信队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE_NAME,"zhangsan");
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE_NAME,"lisi");

        DeliverCallback ackCallback = (consumerTag, message) ->{
            String msg= new String(message.getBody());
            if("msg2".equals(msg)){
                //拒绝消息"msg2"并设置不重新入队
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
                System.out.println("Consumer1收到了消息msg2但是拒绝了");
            }else {
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
                System.out.println("Consumer1收到了消息: "+msg);
            }
        };

        CancelCallback nackCallback = (consumerTag) -> {
            System.out.println("Consumer1接收消息中断");
        };

        //关闭自动应答
        channel.basicConsume(NORMAL_QUEUE,false,ackCallback,nackCallback);

    }
}
