package com.spray

/**
  * Created by IntelliJ IDEA.
  * Author lihongbing on 8/4/2018.
  */
import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http

import scala.concurrent.duration._

object Boot extends App{
  //创建一个名为smartjava的Actor系统

  implicit val system = ActorSystem("smartjava")
  val service = system.actorOf(Props[SJServiceActor], "sj-rest-service")

  // IO需要implicit ActorSystem, ? 需要implicit timeout
  // 绑定 HTTP到指定服务.
  implicit val timeout = Timeout(5.seconds)
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
