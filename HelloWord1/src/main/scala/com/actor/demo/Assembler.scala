package com.actor.demo

import scala.actors.Actor

/**
  * Created by IntelliJ IDEA.
  * Author lihongbing on 5/22/2018.
  */

class Assembler extends Actor {
  var primeNumCount = 0

  def act(): Unit = {
    while (true) {
      receive {
        //收到整数，就组装
        case count: Int =>
          primeNumCount = primeNumCount + count
          println("current prime count is " + primeNumCount)
        case STOP =>
          println("total prime count is " + primeNumCount)
          exit()
      }
    }
  }
}
