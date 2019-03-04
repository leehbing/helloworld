package com.hbase

import java.util

import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, HColumnDescriptor, HTableDescriptor, TableName}

/**
  * @Author: hongbing.li
  * @Date: 9/11/2018 10:37 AM
  */
object ScalaHBaseTest {
  //创建表
  def createHTable(connection: Connection, tablename: String): Unit = {
    //Hbase表模式管理器
    val admin = connection.getAdmin
    //本例将操作的表名
    val tableName = TableName.valueOf(tablename)
    //如果需要创建表
    if (!admin.tableExists(tableName)) {
      //创建Hbase表模式
      val tableDescriptor = new HTableDescriptor(tableName)
      //创建列簇1    artitle
      tableDescriptor.addFamily(new HColumnDescriptor("artitle".getBytes()))
      //创建列簇2    author
      tableDescriptor.addFamily(new HColumnDescriptor("author".getBytes()))
      //创建表
      admin.createTable(tableDescriptor)
      admin.close()
      println("create done.")
    }

  }

  //删除表
  def deleteHTable(connection: Connection, tablename: String): Unit = {
    //本例将操作的表名
    val tableName = TableName.valueOf(tablename)
    //Hbase表模式管理器
    val admin = connection.getAdmin
    if (admin.tableExists(tableName)) {
      admin.disableTable(tableName)
      admin.deleteTable(tableName)
    }
    admin.close()

  }
  /** 清空Hbase表
    * clear a table ,through delete it first and then re-create it
    */
  def clearHTable(connection: Connection, hTable: String): Unit = {
    val admin = connection.getAdmin
    if (admin.tableExists(TableName.valueOf(hTable))) {
      if (admin.isTableEnabled(TableName.valueOf(hTable))) admin.disableTable(TableName.valueOf(hTable))
      admin.deleteTable(TableName.valueOf(hTable))
    }

    val htd = new HTableDescriptor(TableName.valueOf(hTable))
    val hcd = new HColumnDescriptor("f")
    //add  a column family to table
    htd.addFamily(hcd)
    admin.createTable(htd)

  }

  //插入记录
  def insertHTable(connection: Connection, tablename: String, family: String, column: String, key: String, value: String): Unit = {
    try {
      val userTable = TableName.valueOf(tablename)
      val table = connection.getTable(userTable)
      //准备rowkey 的数据
      val p = new Put(key.getBytes)
      //为put操作指定 column 和 value
      p.addColumn(family.getBytes, column.getBytes, value.getBytes())
      //验证可以提交两个clomun？？？？不可以
      // p.addColumn(family.getBytes(),"china".getBytes(),"JAVA for china".getBytes())
      //提交一行
      table.put(p)
      table.close()
    }
  }

  //基于KEY查询某条数据
  def getAResult(connection: Connection, tablename: String, family: String, column: String, key: String): Unit = {
    var table: Table = null
    try {
      val userTable = TableName.valueOf(tablename)
      table = connection.getTable(userTable)
      val g = new Get(key.getBytes())
      val result = table.get(g)
      val value = Bytes.toString(result.getValue(family.getBytes(), column.getBytes()))
      println("key:" + value)
    } finally {
      if (table != null) table.close()

    }

  }

  //删除某条记录
  def deleteRecord(connection: Connection, tablename: String, family: String, column: String, key: String): Unit = {
    var table: Table = null
    try {
      val userTable = TableName.valueOf(tablename)
      table = connection.getTable(userTable)
      val d = new Delete(key.getBytes())
      d.addColumn(family.getBytes(), column.getBytes())
      table.delete(d)
      println("delete record done.")
    } finally {
      if (table != null) table.close()
    }
  }

  //扫描记录
  def scanRecord(connection: Connection, tablename: String, family: String, column: String): Unit = {
    var table: Table = null
    var scanner: ResultScanner = null
    try {
      val userTable = TableName.valueOf(tablename)
      table = connection.getTable(userTable)
      val s = new Scan()
      s.addColumn(family.getBytes(), column.getBytes())
      scanner = table.getScanner(s)
      println("scan...for...")
      var result: Result = scanner.next()
      while (result != null) {
        println("Found row:" + result)
        println("Found value: " + Bytes.toString(result.getValue(family.getBytes(), column.getBytes())))
        result = scanner.next()
      }
    } finally {
      if (table != null)
        table.close()
      scanner.close()
    }
  }

  /**
    * 获取最新的n条记录
    *
    * @param connection
    * @param tablename
    * @param family
    * @param columns
    * @param n     获取最近n条
    */
  def scanRecordRecent(connection: Connection, tablename: String, family: String, columns: Array[String] , n: Int): Unit = {
    var table: Table = null
    var scanner: ResultScanner = null
    try {
      val userTable = TableName.valueOf(tablename)
      table = connection.getTable(userTable)
      val s = new Scan()
      s.addColumn(family.getBytes(), columns(0).getBytes())
        .addColumn(family.getBytes(), columns(1).getBytes())
      scanner = table.getScanner(s)
      println("scan...for...")
      var result: Result = scanner.next()
      while (result != null) {
        println("Found row:" + result)
        println("Found value1: " + Bytes.toString(result.getValue(family.getBytes(), columns(0).getBytes())))
        println("Found value2: " + Bytes.toString(result.getValue(family.getBytes(), columns(1).getBytes())))
        result = scanner.next()
      }
    } finally {
      if (table != null)
        table.close()
      scanner.close()
    }
  }


  def timer[A](blockOfCode: => A )={
    val starttime=System.nanoTime              //系统纳米时间
    val result=blockOfCode
    val endtime=System.nanoTime
    val delta=endtime-starttime
    (result,delta/1000000d)
  }


  def main(args: Array[String]): Unit = {
    //创建一个配置，采用的是工厂方法
    val conf = HBaseConfiguration.create
    val tablename = "cobub3:metadata_test"
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("hbase.zookeeper.quorum", "tdhtest01,tdhtest02,tdhtest03")
    conf.set("hbase.master", "tdhtest01:60000")
    conf.set(TableInputFormat.INPUT_TABLE, tablename)

    try {
      //Connection 的创建是个重量级的工作，线程安全，是操作hbase的入口
      val connection = ConnectionFactory.createConnection(conf)
      //创建表测试
      try {
//        createHTable(connection, "blog")
        //插入数据,重复执行为覆盖

//        insertHTable(connection, "blog", "artitle", "engish", "002", "c++ for me")
//        insertHTable(connection, "blog", "artitle", "engish", "003", "python for me")
//        insertHTable(connection, "blog", "artitle", "chinese", "002", "C++ for china")
        //删除记录
        // deleteRecord(connection,"blog","artitle","chinese","002")
        //扫描整个表
//        scanRecord(connection, "razor:user_online_result_tmp", "f", "total")
//        scanRecordRecent(connection, "razor:user_online_result_tmp", "f", Array("total","increment") , 2)


        val resultTable = connection.getTable(TableName.valueOf("cobub3:metadata_test"))
        val resultList = new util.ArrayList[Put]

        val starttime = System.currentTimeMillis


        for(i<- 0 until 100){ //10个属性类别
//          val put = new Put(Bytes.toBytes("metatype" + i))
          for(j<- 0 until 20) { //每个类型属性值
//            put.addColumn(Bytes.toBytes("f"), Bytes.toBytes("metaC" + j), Bytes.toBytes(1))
//            put.addColumn(Bytes.toBytes("f"), Bytes.toBytes("metaC" + j), Bytes.toBytes(1))
            resultTable.incrementColumnValue(Bytes.toBytes("metatype" + i),Bytes.toBytes("f"), Bytes.toBytes("metaC" + j), 1)
          }
//          resultList.add(put)
//          if (i % 20 == 0) {
//            resultTable.put(resultList)
//            resultList.clear()
//          }
//          if (!resultList.isEmpty) {
//            resultTable.put(resultList)
//          }
        }
        resultTable.close()

        val endtime = System.currentTimeMillis

        println((endtime - starttime))
      } finally {
        connection.close
      }
    }
  }
}
