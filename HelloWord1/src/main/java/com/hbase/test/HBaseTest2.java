package com.hbase.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

public class HBaseTest2 {
	// 声明静态配置
	private static Configuration conf = null;

	static {
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.zookeeper.quorum", "hadooptest1,hadooptest2,hadooptest3");
		conf.set("hbase.master", "hadooptest1:60000");
	}
	// 判断表是否存在
	private static boolean isExist(String tableName) throws IOException {
		HBaseAdmin hAdmin = new HBaseAdmin(conf);
		return hAdmin.tableExists(tableName);
	}

	// 获取一条数据
	public static void getRow(String tableName, String row) throws Exception {
		HTable table = new HTable(conf, tableName);
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
	public static void getAllRows(String tableName) throws Exception {
		HTable table = new HTable(conf, tableName);
		Scan scan = new Scan();
		FilterList colFilterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
		colFilterList.addFilter(
				new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("N"))));
		colFilterList.addFilter(new ColumnPrefixFilter(Bytes.toBytes("NV")));
		colFilterList.addFilter(new ColumnPrefixFilter(Bytes.toBytes("NC")));
		colFilterList.addFilter(new ColumnPrefixFilter(Bytes.toBytes("UV")));
		colFilterList.addFilter(new QualifierFilter(CompareFilter.CompareOp.EQUAL,
				new BinaryComparator(Bytes.toBytes("Session"))));
		colFilterList.addFilter(new ColumnPrefixFilter(Bytes.toBytes("SV")));
		colFilterList.addFilter(new ColumnPrefixFilter(Bytes.toBytes("SC")));
		scan.setFilter(colFilterList);
		ResultScanner results = table.getScanner(scan);
		// 输出结果
		for (Result result : results) {
			for (KeyValue rowKV : result.raw()) {
				System.out.print("行名:" + new String(rowKV.getRow()) + " ");
				System.out.print("时间戳:" + rowKV.getTimestamp() + " ");
				System.out.print("列族名:" + new String(rowKV.getFamily()) + " ");
				System.out.print("列名:" + new String(rowKV.getQualifier()) + " ");
//				System.out.println("值:" + Bytes.toLong(rowKV.getValue()));
				System.out.println("值:" + Bytes.toString(rowKV.getValue()));
			}
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
//			HBaseTest2.getAllRows("razor:user_online_result");
//			System.out.println(HBaseTest.isExist("razor:user_online_result"));

//			System.out.println("**************获取razor:Sum_Statistic数据***************");
//			HBaseTest2.getAllRows("razor:Sum_Statistic");


			System.out.println("**************获取razor:Sum_Statistic数据***************");
			HBaseTest2.isExist("razor:Sum_Statistic");
			System.out.println("**************result::::::***************");


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}



	





































