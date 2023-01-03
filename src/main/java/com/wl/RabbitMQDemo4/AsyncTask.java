package com.wl.RabbitMQDemo4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import com.wl.utils.RabbitMQUtils;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class AsyncTask {
    public static final String TASK_QUEUE4 = "task_queue4";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        channel.queueDeclare(TASK_QUEUE4,true,false,false,null);
        //开启确认发布
        channel.confirmSelect();

        //存放已发送消息的容器,并发安全的map
        ConcurrentSkipListMap<Long,String> outStandingConfirmed = new ConcurrentSkipListMap<>();

        /**
         * deliverTag 每条消息的标记
         * multiple 是否为批量应答
         */
        //发布成功回调函数
        ConfirmCallback ackCallback = (deliveryTag,multiple) -> {
            if(multiple){
                //如果是批量应答的话,就使用headMap来取出(映射,会影响到原来map里的数据)当前tag之前(<=tag)的所有(即批量应答的消息),
                //并对其进行删除(这样map中就会只剩下发布失败的消息)
                ConcurrentNavigableMap<Long, String> headMap = outStandingConfirmed.headMap(deliveryTag);
                headMap.clear();
            }else {
                //若不是批量,则直接对当天tag对应的message进行移除即可
                outStandingConfirmed.remove(deliveryTag);
            }
            System.out.println("发布成功"+deliveryTag);
        };
        //发布失败的回调
        ConfirmCallback nackCallback = (deliveryTag,multiple) ->{
            String tag = outStandingConfirmed.get(deliveryTag);
            System.out.println("tag-"+tag+" 消息发布失败");
        };


        //开启一个异步确认的监听器
        channel.addConfirmListener(ackCallback,nackCallback);
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String message = i + "";
            channel.basicPublish("",TASK_QUEUE4, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            //所有发送成功的消息都需要添加到outStandingConfirmed中(消息的tag作为key,消息的内容作为value)
            outStandingConfirmed.put(channel.getNextPublishSeqNo(),message);
        }
        long end = System.currentTimeMillis();

        System.out.println("异步确认发布发送1000条消息用时用时:"+(end-begin));
        //异步确认发布发送1000条消息用时用时:34

    }
}
