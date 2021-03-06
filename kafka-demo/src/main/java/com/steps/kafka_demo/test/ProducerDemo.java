package com.steps.kafka_demo.test;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author fx
 * @date 2021-12-24 17:46
 */
public class ProducerDemo {

    private static final String topic = "kafkaTopic";

    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put("bootstrap.servers", "10.24.4.236:9092");
        props.put("acks", "0");
        props.put("group.id", "1111");
        props.put("retries", "0");
        //设置key和value序列化方式
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //生产者实例
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        int i = 1;

        // 发送业务消息
        // 读取文件 读取内存数据库 读socket端口
        while (true) {
            Thread.sleep(1000);
            producer.send(new ProducerRecord<String, String>(topic, "key:" + i, "value:" + i));
            System.out.println("key:" + i + " " + "value:" + i);
            i++;
        }
    }

}
