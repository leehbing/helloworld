package com.it18zhang.kafka.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;



/**
 * Created by hongbing.li on 2017/9/5.
 */
public class SimpleHLConsumer {
    private final ConsumerConnector consumer;
    private final String topic;

    public SimpleHLConsumer(String zookeeper, String groupId, String topic) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "500");
        props.put("zookeeper.sync.time.ms", "250");
        props.put("auto.commit.interval.ms", "1000");
        //props.put("auto.offset.reset", "smallest");  //从一开始进行消费   --from-biginning，这个不对，暴力一点就是直接去改zookeeper的节点值：/consumers/g1/offsets/test3/0
                                                        //set /consumers/g1/offsets/test3/0 0
                                                        //set /consumers/g1/offsets/test3/1 0
                                                        //set /consumers/g1/offsets/test3/2 0

        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        this.topic = topic;
    }

    public void testConsumer() {
        Map<String, Integer> topicCount = new HashMap<String, Integer>();
        // Define single thread for topic
        topicCount.put(topic, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);
        List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);
        for (final KafkaStream stream : streams) {
            ConsumerIterator<byte[], byte[]> consumerIte = stream.iterator();
            while (consumerIte.hasNext())  //阻塞，如果debug，这个会单步不下去，只有当有消息的时候，才可以单步下去，然后又停在这里了
                System.out.println("Message from Single Topic :: " + new String(consumerIte.next().message()));
        }
        if (consumer != null)
            consumer.shutdown();
    }

    public static void main(String[] args) {
        SimpleHLConsumer simpleHLConsumer = new SimpleHLConsumer("localhost:2181", "g1", "test3");
        simpleHLConsumer.testConsumer();
    }
}
