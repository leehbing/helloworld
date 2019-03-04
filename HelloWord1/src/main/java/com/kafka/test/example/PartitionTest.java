package com.kafka.test.example;

import java.util.Properties;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 备注： 要先用命令创建topic及partitions 分区数;否则在自定义的分区中如果有大于1的情况下，
 * 发送数据消息到kafka时会报expired due to timeout while requesting metadata from brokers错误
 * ./kafka-topics.sh --create --zookeeper 192.168.1.202:2181 --topic "pruducer_test" --partitions 3 --replication-factor 2
 */
public class PartitionTest {    //producer
    private static Logger LOG = LoggerFactory.getLogger(PartitionTest.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.1.203:9092");

        props.put("retries", 0);
        // props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        // props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("partitioner.class", "com.kafka.test.example.MyPartition"); //引入partition
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("producer_test", "2223132132","test23_60");
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception e) {
                if (e != null)
                    LOG.error("the producer has a error:" + e.getMessage());
                else {
                    LOG.info("The offset of the record we just sent is: " + metadata.offset());
                    LOG.info("The partition of the record we just sent is: " + metadata.partition());
                }
            }
        });
        try {
            Thread.sleep(1000);
            producer.close();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

}
