package com.wl.publishconfirm;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Slf4j
public class MyCallback implements RabbitTemplate.ConfirmCallback , RabbitTemplate.ReturnsCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;


    //被该注解标注的方法会在bean实例化->bean属性赋值完成之后,初始化执行之前执行
    @PostConstruct
    public void init(){
        //因为需要使用rabbitTemplate来进行内部接口方法的调用,需手动对内部接口进行赋值
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机不管是否收到消息都会触发的一个回调方法
     * @param correlationData 保存回调消息的ID和相关信息
     * @param ack 交换机收到消息 ack = true else ack = false
     * @param cause 交换机收到消息 ——> null  else 失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData == null ? "" : correlationData.getId();
        if(ack){
            log.info("交换机收到了id为{}的消息",id);
        }else {
            log.error("交换机没有收到id为{}的消息,原因是{}",id,cause);
        }
    }

    //在传递过程中消息不可达目的地时触发,将消息回退给生产者(到达交换机后无法正确路由到指定队列)
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("消息被交换机退回,routingKey为{},错误原因为{}",returned.getRoutingKey(),returned.getMessage());
    }
}
