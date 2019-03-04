package com.hbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration

/**
  * Created by zh on 2016/8/16.
  */
class ConfigUtil private {

  def createHbaseConfig {
    val conf: Configuration = HBaseConfiguration.create()
    conf.addResource("hbase-site.xml")
    conf
  }

  def createHadoopConfig ={
    val conf: Configuration = new Configuration()
    conf.addResource("core-site.xml")
    conf.addResource("hbase-site.xml")
    conf.addResource("hbase-site.xml")
    conf
  }
}

object ConfigUtil {
  def apply: ConfigUtil = new ConfigUtil()
}

