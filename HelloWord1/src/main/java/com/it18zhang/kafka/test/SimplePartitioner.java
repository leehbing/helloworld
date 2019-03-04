package com.it18zhang.kafka.test;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * Created by hongbing.li on 2017/9/5.
 * 简单分区函数
 */
public class SimplePartitioner implements Partitioner {
    public SimplePartitioner(VerifiableProperties p) {
    }

    @Override
    public int partition(Object key, int numPartitions) {
        int partition = 0;
        int iKey = (Integer)key;
        if (iKey > 0) {
            partition = iKey % numPartitions;
        }
        return partition;
    }
}
