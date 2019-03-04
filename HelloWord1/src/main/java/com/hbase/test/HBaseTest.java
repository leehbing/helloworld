package com.hbase.test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseTest {
	// 声明静态配置
	private static Configuration conf = null;


	static {
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.zookeeper.quorum", "10.240.2.131,10.240.2.134,10.240.2.143");
		conf.set("hbase.master", "10.240.2.131");
	}

	public static String stampToDate(Long s){
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
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
		FilterList filters = new FilterList(FilterList.Operator.MUST_PASS_ONE);
		Filter filter = new ColumnPrefixFilter(Bytes.toBytes("count"));
		filters.addFilter(filter);
		scan.setBatch(10);
		scan.setFilter(filters);
		ResultScanner results = table.getScanner(scan);
		// 输出结果
		for (Result result : results) {
			for (KeyValue rowKV : result.raw()) {
				System.out.print("行名:" + new String(rowKV.getRow()) + " ");
				System.out.print("时间戳:" + rowKV.getTimestamp() + " ");
				System.out.print("列族名:" + new String(rowKV.getFamily()) + " ");
				System.out.print("列名:" + new String(rowKV.getQualifier()) + " ");
				System.out.println("值:" + Bytes.toLong(rowKV.getValue()));
//				System.out.println("值:" + Bytes.toString(rowKV.getValue()));
			}
		}
	}

	// 获取所有过滤后的数据
	public static void getAllRowsByFilter(String tableName) throws Exception {
		HTable table = new HTable(conf, tableName);
		Scan scan = new Scan();

		FilterList filters = new FilterList(FilterList.Operator.MUST_PASS_ONE);

		Filter filter = new ColumnPrefixFilter(Bytes.toBytes("ACCNV_"));
		Filter filter2 = new ColumnPrefixFilter(Bytes.toBytes("NV_"));
		Filter filter3 = new ColumnPrefixFilter(Bytes.toBytes("UV_"));

		filters.addFilter(filter);
		filters.addFilter(filter2);
		filters.addFilter(filter3);

		scan.setBatch(10);
		scan.setFilter(filters);

		scan.setStartRow("10028148_20171001".getBytes("UTF-8"));
		scan.setStopRow("10028148_20171230".getBytes("UTF-8"));
		ResultScanner results = table.getScanner(scan);
		// 输出结果
		for (Result result : results) {
			for (KeyValue rowKV : result.raw()) {
				System.out.print("行名:" + new String(rowKV.getRow()) + " ");
				System.out.print("时间戳:" + stampToDate(rowKV.getTimestamp()) + " ");
				System.out.print("列族名:" + new String(rowKV.getFamily()) + " ");
				System.out.print("列名:" + new String(rowKV.getQualifier()) + " ");
				System.out.println("值:" + Bytes.toLong(rowKV.getValue()));
//				System.out.println("值:" + Bytes.toString(rowKV.getValue()));
			}
		}
	}
	//获取多版本数据
	public static void getMultiVersionRows(String tableName,String rowkey, String[] filterString) throws Exception{
		HTable table = new HTable(conf, tableName);

		Get get = new Get(Bytes.toBytes(rowkey)); // rowkey
		get.setMaxVersions();
		FilterList filters = new FilterList(FilterList.Operator.MUST_PASS_ONE);

		for(int i =0; i < filterString.length; i++){
			filters.addFilter(new ColumnPrefixFilter(Bytes.toBytes(filterString[i])));
		}
//		Filter filter = new ColumnPrefixFilter(Bytes.toBytes("ACCNV_3.0"));
//		Filter filter2 = new ColumnPrefixFilter(Bytes.toBytes("NV_3.0"));
//		Filter filter3 = new ColumnPrefixFilter(Bytes.toBytes("UV_3.0"));
//		filters.addFilter(filter);
//		filters.addFilter(filter2);
//		filters.addFilter(filter3);
		get.setFilter(filters);
//		get.addColumn(Bytes.toBytes("f"), Bytes.toBytes("city"));     //f:city
//		get.addColumn(Bytes.toBytes("f"), Bytes.toBytes("country"));  //f:country
//		get.addColumn(Bytes.toBytes("f"), Bytes.toBytes("region"));
//		get.addColumn(Bytes.toBytes("f"), Bytes.toBytes("isLogin"));
		Result result = table.get(get);
		List<KeyValue> list = result.list();
		for(final KeyValue kv:list){
			// System.out.println("value: "+ kv+ " str: "+Bytes.toString(kv.getValue()));
			System.out.println(String.format("row:%s, family:%s, qualifier:%s, qualifiervalue:%s, timestamp:%s.",
					Bytes.toString(kv.getRow()),
					Bytes.toString(kv.getFamily()),
					Bytes.toString(kv.getQualifier()),
					Bytes.toLong(kv.getValue()),
					stampToDate(kv.getTimestamp())));
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
//			HBaseTest.getAllRows("pmbcba:F_Product_Device");

			System.out.println("**************获取razor:user_online_result数据***************");
//			HBaseTest.getRow("pmbcba:Sum_Statistic","10028148_20171227");
//			HBaseTest.getAllRowsByFilter("pmbcba:Sum_Statistic");
//			System.out.println("**************获取razor:user_online_result数据***************");
//            HBaseTest.getMultiVersionRows("pmbcba:Sum_Statistic","10028148_20171228",new String[]{"ACCNV_3.0","NV_3.0","UV_3.0"});
//            HBaseTest.getMultiVersionRows("pmbcba:Sum_Statistic","10028148_20171229",new String[]{"ACCNV_3.0","NV_3.0","UV_3.0"});
//			System.out.println("**************获取razor:user_online_result数据***************");
//			HBaseTest.getMultiVersionRows("pmbcba:Sum_Statistic","10028142_20171228",new String[]{"ACCNV_5.0","NV_5.0","UV_5.0"});
//			HBaseTest.getMultiVersionRows("pmbcba:Sum_Statistic","10028142_20171229",new String[]{"ACCNV_5.0","NV_5.0","UV_5.0"});
			System.out.println("**************获取razor:user_online_result数据***************");
			HBaseTest.getMultiVersionRows("pmbcba:Sum_Statistic","10028509_20171229",new String[]{"ACCNV_3.0","NV_3.0","UV_3.0"});
			HBaseTest.getMultiVersionRows("pmbcba:Sum_Statistic","10028509_20171230",new String[]{"ACCNV_3.0","NV_3.0","UV_3.0"});
			HBaseTest.getMultiVersionRows("pmbcba:Sum_Statistic","10028509_20180101",new String[]{"ACCNV_3.0","NV_3.0","UV_3.0"});
			HBaseTest.getMultiVersionRows("pmbcba:Sum_Statistic","10028509_20180102",new String[]{"ACCNV_3.0","NV_3.0","UV_3.0"});
			HBaseTest.getMultiVersionRows("pmbcba:Sum_Statistic","10028509_20180103",new String[]{"ACCNV_3.0","NV_3.0","UV_3.0"});
			HBaseTest.getMultiVersionRows("pmbcba:Sum_Statistic","10028509_20180104",new String[]{"ACCNV_3.0","NV_3.0","UV_3.0"});
//            HBaseTest.getMultiVersionRows("pmbcba:Sum_Statistic","10028148_test");
//            HBaseTest.getAllRows("pmbcba:F_Product_Device");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
