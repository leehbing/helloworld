/**
  *<dependency>
			<groupId>commons-httpclient</groupId>
			<artifactId>commons-httpclient</artifactId>
			<version>3.1</version>
		</dependency>

  用httpcomponets一直报错，上面一个可以的
      <dependency>
      <groupId>org.apache.httpcomponents</groupId>
      <artifactId>httpclient</artifactId>
      <version>4.5.3</version>
    </dependency>

https://zdatainc.com/2014/08/real-time-streaming-apache-spark-streaming/
https://stackoverflow.com/questions/41799578/restapi-service-call-from-spark-streaming
https://stackoverflow.com/questions/25725539/how-do-you-perform-blocking-io-in-apache-spark-job
https://stackoverflow.com/questions/37593628/spark-streaming-for-task-parallelization


spark的task中执行block io操作（比如http，针对每个计算结果，进行微信推送），可以像ajax一样，直接发出去就不管了吗？如果可以这样，就不堵塞了

测试结论如下：每个task（占一个core）任务中启动新的线程还是在这个core上执行，所以还是block操作。目前唯一方案：
1、考虑这个event topic的分区数增大，KafkaUtils创建rdd的分区数就会变大。
2、创建完的kafkaRDD可以利用repatition增大分区
3、spark-submit的时候增加core数，executor数，反正就是能增加task数量就增加task的数量
4、spark的配置文件里面加上：spark.locality.wait=0
5、就担心一个task里面需要推送的消息比较大，结果这个task时间太长
6、还好江苏银行这个需求的数据量不算大，每天几千条日志而已
  */

import com.fasterxml.jackson.core.JsonParseException
import kafka.serializer.{DefaultDecoder, StringDecoder}
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.PostMethod
import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{Logging, SparkConf}
import org.json4s.jackson.JsonMethods.parse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


/**
  * Created by IntelliJ IDEA.
  * Counting data with Spark streaming.
  * Time:   6/30/15 7:46 AM
  *
  * @author jianghe.cao
  */
object Streaming_http_Test extends Logging {

  /**
    * main() method.
    *
    * @param args No any args used.
    */
  def main(args: Array[String]) {
    // Initialize spark conf
    val sparkConf = new SparkConf().setAppName("Count").set("spark.ui.port", "4050")
      .set("spark.executor.extraJavaOptions", "-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8")
      .set("spark.serializer", classOf[KryoSerializer].getName)


    // Initialize spark streaming context
    val ssc = new StreamingContext(sparkConf,
      Seconds(sparkConf.getInt("spark.count.streaming.batch.duration", 10)))
    ssc.checkpoint(sparkConf.get("spark.count.streaming.clientdata.checkpoint"))





    // Kafka configuration
    val kafkaParams = Map("metadata.broker.list" ->
      sparkConf.get("spark.count.kafka.metadata.broker.list", "127.0.0.1:9092"),
      "zookeeper.connect" -> sparkConf.get("spark.count.zookeeper.quorum", "127.0.0.1"),
      "group.id" -> sparkConf.get("spark.count.kafka.groupid", "cloudera_mirrormaker"),
      "zookeeper.connection.timeout.ms" -> "10000",
      "num.partitions" -> sparkConf.get("spark.count.kafka.topics.partition.num", "2"))


    val evTopics = sparkConf.get("spark.count.kafka.event.topics", "event").split(',').toSet



    // Pull events from Kafka
    val events = KafkaUtils.createDirectStream[Array[Byte], String, DefaultDecoder, StringDecoder](
      ssc, kafkaParams, evTopics).map(_._2)
//      .repartition(kafkaParams.get("num.partitions").mkString.toInt)


    // Cleanup data
    val (eventMaps) = cleanupData(events)

    // Count data and write
    countAndWriteToHBase(eventMaps)




    sys.ShutdownHookThread {
      // Gracefully stop spark streaming application
      logInfo("Gracefully stopping Spark Streaming Application")
      ssc.stop(stopSparkContext = true, stopGracefully = true)
      logInfo("Application stopped")
    }

    // Start Spark streaming
    ssc.start()
    ssc.awaitTermination()
  }


  /**
    * Cleanup data
    *

    */
  private def cleanupData(events: DStream[String]): (DStream[Map[String, String]]) = {

    // events
    val eventMaps = events.map(toMap(_)).filter { j =>
//      j.containsKeys("productid", "channelid", "version", "localtime", "acc", "deviceid") &&
//        Utils.isDateValid(j("localtime"))
      true
    }


    // Return a tuple
    (eventMaps)

  }


  private def countAndWriteToHBase(eventMaps: DStream[Map[String, String]]): Unit = {
    //    eventMaps.mapPartitions(mapEvents(_)).foreachRDD(_.foreachPartition(flushToRedis(_, redisConf, dBConf)))
    eventMaps.foreachRDD(_.foreachPartition(flushToRedis(_)))
  }

  private def flushToRedis(p: Iterator[Map[String, String]]): Unit = {

    val client = new HttpClient();
    val url = "http://192.168.1.217:8080/mobile-analysis/getchannelproductinfo.json";
    val method = new PostMethod(url);
    //method.addParameter("type", "1");
    //method.addParameter("url", "");
    //method.addParameter("product", "urs");
    //      method.addParameter("username","xxxxxxx@126.com");
    //      method.addParameter("password", "xxxxxx");


//    p.foreach(jMap => {
//
//      val tmp = Future {
//        client.executeMethod(method)
//        println(Thread.currentThread().getName() + "http's response =====>>>>" + method.getResponseBodyAsString())
//        Thread.sleep(15000)
//        //        println("value: " + jMap)
//      }
//
//      tmp onComplete {
//        case _ => println("complete.......")
//      }
//      tmp onSuccess  {
//        case _ => println("success.......")
//      }
//      tmp onFailure   {
//        case _ => println("failure.......")
//      }
//      println("out of future: ")
//    })

    val futures = p.map(jMap => {
      Future {
        println("start http......")
        client.executeMethod(method)
        println(Thread.currentThread().getName() + "http's response =====>>>>" + method.getResponseBodyAsString())
        Thread.sleep(15000)
      }
//      println("out of future: ")
    }).foreach{f=>
      Await.result(f,Duration.Inf)

    }


  }

  def toMap(j: String): Map[String, String] = {
    try {
      parse(j).values.asInstanceOf[Map[String, AnyRef]].mapValues(Some(_).mkString.trim)
        .withDefaultValue("unknown")
    } catch {
      case ex: JsonParseException => Map.empty
    }
  }

}
