package com.hbase

/**
  * Created by leehbing on 2017/4/12.
  *
  * 1、spark如何利用saveAsHadoopDataset(writeToHBase1)和saveAsNewAPIHadoopDataset(writeToHBase2)将RDD写入hbase
    2、spark从hbase中读取数据并转化为RDD


    spark应用需要连接到zookeeper集群，然后借助zookeeper访问hbase。一般可以通过两种方式连接到zookeeper：
    第一种是将hbase-site.xml文件加入classpath
    第二种是在HBaseConfiguration实例中设置
    如果不设置，默认连接的是localhost:2181会报错：connection refused
    本文使用的是第二种方式。
  */

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.apache.spark.{SparkConf, SparkContext}


//使用saveAsHadoopDataset将RDD写入HBase
object WriteToHBase1 {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("HBaseTest").setMaster("local")
    val sc = new SparkContext(sparkConf)

    val conf = HBaseConfiguration.create()
    //设置zooKeeper集群地址，也可以通过将hbase-site.xml导入classpath，但是建议在程序里这样设置
    conf.set("hbase.zookeeper.quorum","slave1,slave2,slave3")
    //设置zookeeper连接端口，默认2181
    conf.set("hbase.zookeeper.property.clientPort", "2181")

    val tablename = "account"

    //初始化jobconf，TableOutputFormat必须是org.apache.hadoop.hbase.mapred包下的！
    val jobConf = new JobConf(conf)
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    jobConf.set(TableOutputFormat.OUTPUT_TABLE, tablename)

    val indataRDD = sc.makeRDD(Array("1,jack,15","2,Lily,16","3,mike,16")) //rowkey cf:name cf:age

    val rdd = indataRDD.map(_.split(',')).map{arr=>{
      /*一个Put对象就是一行记录，在构造方法中指定主键
       * 所有插入的数据必须用org.apache.hadoop.hbase.util.Bytes.toBytes方法转换
       * Put.add方法接收三个参数：列族，列名，数据
       */
      val put = new Put(Bytes.toBytes(arr(0).toInt))
      put.add(Bytes.toBytes("cf"),Bytes.toBytes("name"),Bytes.toBytes(arr(1)))
      put.add(Bytes.toBytes("cf"),Bytes.toBytes("age"),Bytes.toBytes(arr(2).toInt))
      //转化成RDD[(ImmutableBytesWritable,Put)]类型才能调用saveAsHadoopDataset
      (new ImmutableBytesWritable, put)
    }}
    rdd.saveAsHadoopDataset(jobConf)
    sc.stop()
  }
}

//使用saveAsNewAPIHadoopDataset将RDD写入HBase
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job

object WriteToHBase2 {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("HBaseTest").setMaster("local")
    val sc = new SparkContext(sparkConf)

    val tablename = "account"

    sc.hadoopConfiguration.set("hbase.zookeeper.quorum","slave1,slave2,slave3")
    sc.hadoopConfiguration.set("hbase.zookeeper.property.clientPort", "2181")
    sc.hadoopConfiguration.set(org.apache.hadoop.hbase.mapreduce.TableOutputFormat.OUTPUT_TABLE, tablename)

    val job = new Job(sc.hadoopConfiguration)
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Result])
    job.setOutputFormatClass(classOf[org.apache.hadoop.hbase.mapreduce.TableOutputFormat[ImmutableBytesWritable]])

    val indataRDD = sc.makeRDD(Array("1,jack,15","2,Lily,16","3,mike,16"))
    val rdd = indataRDD.map(_.split(',')).map{arr=>{
      val put = new Put(Bytes.toBytes(arr(0)))
      put.add(Bytes.toBytes("cf"),Bytes.toBytes("name"),Bytes.toBytes(arr(1)))
      put.add(Bytes.toBytes("cf"),Bytes.toBytes("age"),Bytes.toBytes(arr(2).toInt))
      (new ImmutableBytesWritable, put)
    }}
    rdd.saveAsNewAPIHadoopDataset(job.getConfiguration())
  }
}


import org.apache.hadoop.hbase.client.HBaseAdmin
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, HTableDescriptor, TableName}

//官方提供的例子,从HBase中将数据读取为RDD
object ReadFromHBase {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("HBaseTest").setMaster("local")
    val sc = new SparkContext(sparkConf)

    val tablename = "account"
    val conf = HBaseConfiguration.create()
    //设置zooKeeper集群地址，也可以通过将hbase-site.xml导入classpath，但是建议在程序里这样设置
    conf.set("hbase.zookeeper.quorum","slave1,slave2,slave3")
    //设置zookeeper连接端口，默认2181
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set(TableInputFormat.INPUT_TABLE, tablename)

    // 如果表不存在则创建表
    val admin = new HBaseAdmin(conf)
    if (!admin.isTableAvailable(tablename)) {
      val tableDesc = new HTableDescriptor(TableName.valueOf(tablename))
      admin.createTable(tableDesc)
    }

    //读取数据并转化成rdd
    val hBaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    val count = hBaseRDD.count()
    println(count)
    hBaseRDD.foreach{case (_,result) =>{
      //获取行键
      val key = Bytes.toString(result.getRow)
      //通过列族和列名获取列
      val name = Bytes.toString(result.getValue("cf".getBytes,"name".getBytes))     //cf:name
      val age = Bytes.toInt(result.getValue("cf".getBytes,"age".getBytes))        //cf:age
      println("Row key:"+key+" Name:"+name+" Age:"+age)
    }}

    sc.stop()
    admin.close()
  }
}





