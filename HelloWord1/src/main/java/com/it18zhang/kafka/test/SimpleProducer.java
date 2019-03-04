package com.it18zhang.kafka.test;

import java.util.Properties;


import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import kafka.producer.ProducerConfig;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * Created by hongbing.li on 2017/9/3.
 */
//@SuppressWarnings("deprecation")
public class SimpleProducer {

    public static void main(String[] args) {
        test1();
//        test2();


    }


    public static void test1() {
        Properties props = new Properties();
        props.put("metadata.broker.list", "slave01:9092,slave02:9092,slave03:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
        props.put("partitioner.class", "com.it18zhang.kafka.test.SimplePartitioner");//带有分区函数的生产者，将消息发送到指定分区,指定分区函数

        Producer<Integer, String> producer = new Producer<Integer, String>(new ProducerConfig(props));  //已不推荐使用Producer，推荐org.apache.kafka.clients.producer.KafkaProducer<K,V>
        String topic = "producer_test";
        String messageStr = "helloworld";
        KeyedMessage<Integer, String> data = new KeyedMessage<Integer, String>(topic, messageStr);//如果没有这个主题，会自动创建
        producer.send(data);
        producer.close();
    }

    public static void test2() {   //这是新的api，推荐使用
        Properties props = new Properties();
        props.put("bootstrap.servers", "tw-slave01:9092,tw-slave02:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("producer.type", "sync");
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("partitioner", "com.it18zhang.kafka.test.SimplePartitioner2");//带有分区函数的生产者，将消息发送到指定分区,指定分区函数

        org.apache.kafka.clients.producer.Producer<String, String> producer = new KafkaProducer<String, String>(props);
        for (int i = 0; i < 100; i++) {
            ProducerRecord<String, String> rec = new ProducerRecord<String, String>("producer_test", Integer.toString(i), Integer.toString(i));
            producer.send(rec, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    System.out.println("ack !!");//producer发给broker，broker立刻向客户端发送确认回执，客户端会回调
                }
            });
        }
        producer.close();
    }

}


