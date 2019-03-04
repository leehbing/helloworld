package com.kafka.test;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by hongbing.li on 2017/4/13.
 */


public class KafkaClient {

    public static void main(String[] args) {
        JProducer pro = new JProducer("producer_test");
        pro.start();

//        JConsumer con = new JConsumer("producer_test");
//        con.start();
    }
}

class JProducer extends Thread {

    private Producer<Integer, String> producer;
    private String topic;
    private Properties props = new Properties();
    private final int SLEEP = 1000 * 3;

    public JProducer(String topic) {
        props.put("serializer.class", StringEncoder.class.getName());
        props.put("metadata.broker.list", "tw-slave01:9092,tw-slave02:9092");
        producer = new Producer<Integer, String>(new ProducerConfig(props));
        this.topic = topic;
    }

    @Override
    public void run() {
        int i = 5000;
        while (true) {
            String msg = new String("Message_" + i);
            System.out.println("Send->[" + msg + "]");
            producer.send(new KeyedMessage<Integer, String>(topic, "1"));
            i++;
            try {
                sleep(SLEEP);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}

class JConsumer extends Thread {    //接收数据

    private ConsumerConnector consumer;
    private String topic;
    private final int SLEEP = 1000 * 3;

    public JConsumer(String topic) {
        Properties props = new Properties();
        props.put("zookeeper.connect", "slave01:2181,slave02:2181");
        props.put("metadata.broker.list", "slave01:9092,slave02:9092,slave03:9092");
        props.put("group.id", "test_group1");// 必须要使用别的组名称，
                                            // 如果生产者和消费者都在同一组，则不能访问同一组内的topic数据
        props.put("zookeeper.session.timeout.ms", "40000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        this.topic = topic;
    }

    @Override
    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(1)); // 一次从主题中获取一个数据
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = messageStreams.get(topic).get(0); // 获取每次接收到的这个数据
        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
        while (iterator.hasNext()) {
            System.out.println("Receive->[" + new String(iterator.next().message()) + "]");
            try {
                sleep(SLEEP);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}