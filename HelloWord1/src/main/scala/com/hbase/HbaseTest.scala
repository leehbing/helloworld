package com.hbase

import java.util

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{Cell, HColumnDescriptor, HTableDescriptor, KeyValue}

import collection.JavaConverters._

/**
  * Created by zh on 2016/8/16.
  */
object HbaseTest {

  private val conf: Configuration = ConfigUtil.apply.createHadoopConfig

  def isExist(tableName: String) {
    val hAdmin: HBaseAdmin = new HBaseAdmin(conf)
    hAdmin.tableExists(tableName)
  }

  def createTable(tableName: String, columnFamilys: Array[String]): Unit = {
    val hAdmin: HBaseAdmin = new HBaseAdmin(conf)
    if (hAdmin.tableExists(tableName)) {
      println("表" + tableName + "已经存在")
      return
    } else {
      val tableDesc: HTableDescriptor = new HTableDescriptor(tableName)
      for (columnFaily <- columnFamilys) {
        tableDesc.addFamily(new HColumnDescriptor(columnFaily))
      }
      hAdmin.createTable(tableDesc)
      println("创建表成功")
    }
  }

  def deleteTable(tableName: String): Unit = {
    val admin: HBaseAdmin = new HBaseAdmin(conf)
    if (admin.tableExists(tableName)) {
      admin.disableTable(tableName)
      admin.deleteTable(tableName)
      println("删除表成功!")
    } else {
      println("表" + tableName + " 不存在")
    }
  }

  def addRow(tableName: String, row: String, columnFaily: String, column: String, value: String): Unit = {
    val table: HTable = new HTable(conf, tableName)
    val put: Put = new Put(Bytes.toBytes(row))
    put.add(Bytes.toBytes(columnFaily), Bytes.toBytes(column), Bytes.toBytes(value))
    table.put(put)
  }

  def delRow(tableName: String, row: String): Unit = {
    val table: HTable = new HTable(conf, tableName)
    val delete: Delete = new Delete(Bytes.toBytes(row))
    table.delete(delete)
  }

  def delMultiRows(tableName: String, rows: Array[String]): Unit = {
    val table: HTable = new HTable(conf, tableName)
    val deleteList = for (row <- rows) yield new Delete(Bytes.toBytes(row))
    table.delete(deleteList.toSeq.asJava)
  }

  def getRow(tableName: String, row: String): Unit = {
    val table: HTable = new HTable(conf, tableName)
    val get: Get = new Get(Bytes.toBytes(row))
    val result: Result = table.get(get)
    for (rowKv <- result.raw()) {
      println(new String(rowKv.getFamily))
      println(new String(rowKv.getQualifier))
      println(rowKv.getTimestamp)
      println(new String(rowKv.getRow))
      println(new String(rowKv.getValue))
    }
  }

  def getAllRows(tableName: String): Unit = {
    val table: HTable = new HTable(conf, tableName)
    val results: ResultScanner = table.getScanner(new Scan())
    val it: util.Iterator[Result] = results.iterator()
    while (it.hasNext) {
      val next: Result = it.next()
      for(kv <- next.raw()){
        println(new String(kv.getRow))
        println(new String(kv.getFamily))
        println(new String(kv.getQualifier))
        println(new String(kv.getValue))
        println(kv.getTimestamp)
        println("---------------------")
      }

      //      val cells: Array[Cell] = next.rawCells()
      //      for (cell <- cells) {
      //        println(new String(cell.getRowArray)+" row")
      //        println(new String(cell.getFamilyArray))
      //        println(new String(cell.getQualifierArray))
      //        println(new String(cell.getValueArray))
      //        println(cell.getTimestamp)
      //        println("---------------------")
      //      }


    }
  }


  def main(args: Array[String]) {
    //TestHbaeJavaApi.createTable("testApi",Array("info","two"))
    //TestHbaeJavaApi.addRow("testApi","row2","info","get","getTwo")
    //TestHbaeJavaApi.delRow("testApi","row2")

    //TestHbaeJavaApi.getRow("testApi","row1")
//    this.getAllRows("razor:sumevent")


//    val table: HTable = new HTable(conf,"razor:test")
//
//    val result = table.incrementColumnValue("rowkey2".getBytes,"f".getBytes,"count".getBytes,1l) //计数器，如果hbase表里没有此rowkey，则会创建这一条，如果有，会递增。返回值是increment后的结果
//    println(result)

//    getRow("razor:sumevent","10028192-20170925-$$_android1-1.0")
//    getAllRows("pmbcba:sumevent")
//    addRow("pmbcba:sumevent","rowkey2","f","column1","value1")

    val g=new Get("000_10014847_359997437852420".getBytes())
    g.addColumn("f".getBytes(),"NZone".getBytes())
    g.addColumn("f".getBytes(),"NHour".getBytes())
    val pdTable: HTable = new HTable(conf, "gd:F_Product_Device")

    val rNew = pdTable.get(g)

    println(Bytes.toString(rNew.value()))

    println(rNew.containsNonEmptyColumn("f".getBytes(), Bytes.toBytes("NZone")) )

    println(rNew.containsNonEmptyColumn("f".getBytes(), Bytes.toBytes("NHour")))
    println(rNew.containsNonEmptyColumn("f".getBytes(), Bytes.toBytes("fsdfdf")))
    println(rNew.containsNonEmptyColumn("f".getBytes(), Bytes.toBytes("NInfo")))



  }

}
