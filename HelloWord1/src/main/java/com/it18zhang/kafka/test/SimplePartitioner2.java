package com.it18zhang.kafka.test;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * Created by hongbing.li on 2017/9/5.
 */
public class SimplePartitioner2 implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        return 0;
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }

    @Override
    public void close() {

    }
}
