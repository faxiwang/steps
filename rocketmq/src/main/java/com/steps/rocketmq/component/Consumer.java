package com.steps.rocketmq.component;

import com.steps.rocketmq.model.ParkPayMessge;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author fx
 * @date 2021-12-30 11:42
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "parkpay_message_consumer", topic = "parkpayMessage")
public class Consumer implements RocketMQListener<ParkPayMessge> {
    @Override
    public void onMessage(ParkPayMessge msg) {
       log.info(msg.getMsg());
    }
}
