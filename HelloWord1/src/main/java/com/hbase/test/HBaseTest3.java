package com.hbase.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class HBaseTest3 {
    // 声明静态配置
    private static Configuration HBASE_CONFIG = null;

    static {
        HBASE_CONFIG = HBaseConfiguration.create();
        HBASE_CONFIG.set("hbase.zookeeper.property.clientPort", "2181");
        HBASE_CONFIG.set("hbase.zookeeper.quorum", "tdhtest01,tdhtest02,tdhtest03");
        HBASE_CONFIG.set("hbase.master", "tdhtest01:60000");
    }


    // 判断表是否存在
    private static boolean isExist(String tableName) throws IOException {
        HBaseAdmin hAdmin = new HBaseAdmin(HBASE_CONFIG);
        return hAdmin.tableExists(tableName);
    }

    // 获取一条数据
    public static void getRow(String tableName, String row) throws Exception {
        HTable table = new HTable(HBASE_CONFIG, tableName);
        Get get = new Get(Bytes.toBytes(row));
        Result result = table.get(get);
        // 输出结果,raw方法返回所有keyvalue数组
        for (KeyValue rowKV : result.raw()) {
            System.out.print("行名:" + new String(rowKV.getRow()) + " ");
            System.out.print("时间戳:" + rowKV.getTimestamp() + " ");
            System.out.print("列族名:" + new String(rowKV.getFamily()) + " ");
            System.out.print("列名:" + new String(rowKV.getQualifier()) + " ");
            System.out.println("值:" + Bytes.toLong(rowKV.getValue()));
        }
    }

    // 获取所有数据
    public static void getAllRowsValueLong(String tableName) throws Exception {
        HTable table = new HTable(HBASE_CONFIG, tableName);
        Scan scan = new Scan();
        ResultScanner results = table.getScanner(scan);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");

        // 输出结果
        for (Result result : results) {
            for (KeyValue rowKV : result.raw()) {
                System.out.print("行名:" + new String(rowKV.getRow()) + " ");
                System.out.print("时间戳:" + format.format(rowKV.getTimestamp()) + " ");
                System.out.print("列族名:" + new String(rowKV.getFamily()) + " ");
                System.out.print("列名:" + new String(rowKV.getQualifier()) + " ");
                System.out.println("值:" + Bytes.toLong(rowKV.getValue()));
            }
        }
    }

    // 获取所有数据
    public static void getAllRowsValueString(String tableName) throws Exception {
        HTable table = new HTable(HBASE_CONFIG, tableName);
        Scan scan = new Scan();
        ResultScanner results = table.getScanner(scan);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");

        // 输出结果
        for (Result result : results) {
            for (KeyValue rowKV : result.raw()) {
//				if(new String(rowKV.getQualifier()).equalsIgnoreCase("event_duration")) {
                System.out.print("行名:" + new String(rowKV.getRow()) + " ");
                System.out.print("时间戳:" + format.format(rowKV.getTimestamp()) + " ");
                System.out.print("列族名:" + new String(rowKV.getFamily()) + " ");
                System.out.print("列名:" + new String(rowKV.getQualifier()) + " ");
                System.out.println("值:" + Bytes.toString(rowKV.getValue()));
//				}
            }
        }
    }

    // 获取所有数据
    public static void getAllRowsValueString2(String tableName) throws Exception {
        HTable table = new HTable(HBASE_CONFIG, tableName);
        Scan scan = new Scan();
        ResultScanner results = table.getScanner(scan);

        // 输出结果
        for (Result result : results) {
            for (KeyValue rowKV : result.raw()) {
                if (new String(rowKV.getQualifier()).equalsIgnoreCase("sequence")) {
                    System.out.print("行名:" + new String(rowKV.getRow()) + " ");
//					System.out.print("时间戳:" + format.format(rowKV.getTimestamp()) + " ");
                    System.out.print("列族名:" + new String(rowKV.getFamily()) + " ");
                    System.out.print("列名:" + new String(rowKV.getQualifier()) + " ");
                    if (new String(rowKV.getQualifier()).equalsIgnoreCase("count")) {
                        System.out.println("值:" + Bytes.toLong(rowKV.getValue()));
                    }
                    if (new String(rowKV.getQualifier()).equalsIgnoreCase("globalUserId")) {
                        System.out.println("值:" + Bytes.toString(rowKV.getValue()));
                    }
                    if (new String(rowKV.getQualifier()).equalsIgnoreCase("sequence")) {
                        System.out.println("值:" + Bytes.toLong(rowKV.getValue()));
                    }
                }
            }
        }
    }

    // 获取所有数据
    public static void getAllRowsValue(String tableName) throws Exception {
        HTable table = new HTable(HBASE_CONFIG, tableName);
        Scan scan = new Scan();
        ResultScanner results = table.getScanner(scan);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");

        // 输出结果
        for (Result result : results) {
            for (KeyValue rowKV : result.raw()) {
                System.out.print("行名:" + new String(rowKV.getRow()) + " ");
                System.out.print("时间戳:" + format.format(rowKV.getTimestamp()) + " ");
                System.out.print("列族名:" + new String(rowKV.getFamily()) + " ");
                String qualifer = new String(rowKV.getQualifier());
                System.out.print("列名:" + qualifer + " ");
                if (qualifer.equalsIgnoreCase("city") || qualifer.equalsIgnoreCase("region") || qualifer.equalsIgnoreCase("country")) {
                    System.out.println("值:" + Bytes.toString(rowKV.getValue()));
                } else {
                    System.out.println("值:" + Bytes.toLong(rowKV.getValue()));
                }
            }
        }
    }

    //获取多版本数据
    public static void getMultiVersionRows(String tableName) throws Exception {
        HTable table = new HTable(HBASE_CONFIG, tableName);

        Get get = new Get(Bytes.toBytes("1f7_10028147_d7c10df6358fc59452402d19a2933631")); // rowkey
        get.setMaxVersions();
        get.addColumn(Bytes.toBytes("f"), Bytes.toBytes("city"));     //f:city
        get.addColumn(Bytes.toBytes("f"), Bytes.toBytes("country"));  //f:country
        get.addColumn(Bytes.toBytes("f"), Bytes.toBytes("region"));
        get.addColumn(Bytes.toBytes("f"), Bytes.toBytes("isLogin"));
        Result result = table.get(get);
        List<KeyValue> list = result.list();
        for (final KeyValue kv : list) {
            // System.out.println("value: "+ kv+ " str: "+Bytes.toString(kv.getValue()));
            System.out.println(String.format("row:%s, family:%s, qualifier:%s, qualifiervalue:%s, timestamp:%s.",
                    Bytes.toString(kv.getRow()),
                    Bytes.toString(kv.getFamily()),
                    Bytes.toString(kv.getQualifier()),
                    Bytes.toString(kv.getValue()),
                    kv.getTimestamp()));
        }

    }


    // 主函数
    public static void main(String[] args) {
        try {
//			HBaseTest.getRow("razor:weeklyactivity", "10028142_20161231");
            //		HBaseTest.getRow("razor:monthlyactivity", "10028142_20170101");

//			System.out.println("**************获取razor:F_Product_Device所有数据***************");
//			HBaseTest.getAllRows("razor:Sum_Statistic");
////			System.out.println("**************获取razor:event_device所有数据***************");
////			HBaseTest.getAllRows("razor:event_device");
//			System.out.println("**************获取razor:sumevent所有数据***************");
//			HBaseTest.getAllRows("razor:sumevent");


//			System.out.println("**************获取razor:event_response_duration数据***************");
//			HBaseTest.getAllRows("razor:event_response_duration");
//			System.out.println("**************获取razor:user_online_realtime数据***************");
//			HBaseTest.getAllRows("razor:user_online_realtime");
//			System.out.println("**************获取razor:user_online_realtime所有版本数据***************");
//			HBaseTest.getMultiVersionRows("razor:user_online_realtime");
// 			System.out.println("**************获取razor:user_online_result数据***************");
            //HBaseTest3.getAllRowsValueLong("razor:sumevent");
//			System.out.println("**************获取razor:user_online_realtime数据***************");
//			HBaseTest3.getAllRowsValueString("razor:user_online_realtime");
//			System.out.println("**************获取razor:user_online_result数据***************");
//			HBaseTest3.getAllRowsValueString("razor:user_online_result");
//			System.out.println("**************获取razor:user_online_result_tmp数据***************");
//			HBaseTest3.getAllRowsValueString("razor:user_online_result_tmp");
//			System.out.println("**************获取razor:user_online_result2数据***************");
//			HBaseTest3.getAllRowsValueString("razor:user_online_result2");
            System.out.println("**************获取razor:event_response_duration数据***************");
            HBaseTest3.getAllRowsValueString2("cobub3:user_identifier");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
