package com.hbase.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HBaseTest4 {
    // 声明静态配置
    private static Configuration conf = null;

    static {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.zookeeper.quorum", "tw-master,tw-slave01,tw-slave02");
        conf.set("hbase.master", "tw-master");
    }




    // 批量插入数据
    public static void batchPut(String tableName) throws Exception {
        HTable table = new HTable(conf, tableName);
        // vv BatchExample
        List<Row> batch = new ArrayList<Row>(); // co BatchExample-1-CreateList Create a list to hold all values.

        int m = 10000;   //总共10个app，每个app有20万个事件，共30天的数据，20170801~20170831
        int n = 10;
        for (int b = 0; b < 1; b++) {
//            String productId = "100148" + String.format("%02d", b + 42);
            String productId = "10014847";
            for (int j = 1; j < 32; j++) {
                String day = "201708" + String.format("%02d", j);
                System.out.println(day);
                for (int i = 0; i < m; i++) {
                    Put put = new Put(Bytes.toBytes(productId + "-" + day + "-register199999abc" +i + "-3.0"));
                    put.addColumn(Bytes.toBytes("f"), Bytes.toBytes("count"), Bytes.toBytes("2")); // co BatchExample-2-AddPut Add a Put instance.
                    batch.add(put);

                }
                table.batch(batch);
                batch.clear();
            }
        }


//        Object[] results = new Object[batch.size()]; // co BatchExample-6-CreateResult Create result array.
//        table.batch(batch, results);
//        batch.clear();
        table.close();

    }


    // 主函数
    public static void main(String[] args) {
        try {

            HBaseTest4.batchPut("gd:test");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
