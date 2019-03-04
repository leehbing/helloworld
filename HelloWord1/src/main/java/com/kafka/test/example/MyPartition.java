package com.kafka.test.example;

import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyPartition implements Partitioner {   //实现org.apache.kafka.clients.producer.Partitioner 分区接口，以实现自定义的消息分区
    private static Logger LOG = LoggerFactory.getLogger(MyPartition.class);
    public MyPartition() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // TODO Auto-generated method stub

    }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // TODO Auto-generated method stub
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();
        int partitionNum = 0;
        try {
            partitionNum = Integer.parseInt((String) key);
        } catch (Exception e) {
            partitionNum = key.hashCode() ;
        }
        LOG.info("the message sendTo topic:"+ topic+" and the partitionNum:"+ partitionNum);
        return Math.abs(partitionNum  % numPartitions);
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

}
