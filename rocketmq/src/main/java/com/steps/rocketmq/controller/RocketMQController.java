package com.steps.rocketmq.controller;

import com.steps.rocketmq.model.ParkPayMessge;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fx
 * @date 2021-12-30 11:39
 */
@RestController
@RequestMapping("/rocketMQ")
@Slf4j
public class RocketMQController {
    @Resource
    private RocketMQTemplate rocketMQTemplate;


    @GetMapping("/send")
    public String send() throws MQClientException {
        ParkPayMessge msg = new ParkPayMessge();
        msg.setId(1l);
        msg.setDateTime(System.currentTimeMillis());
        msg.setMsg("这是一条停车支付消息");
        //发送消息
        rocketMQTemplate.convertAndSend("parkpayMessage", msg);
        log.info("我是生产者，我完成了数据的发送");
        return "OK";
    }
}
