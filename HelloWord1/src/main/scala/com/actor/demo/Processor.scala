package com.actor.demo

import scala.actors.Actor

/**
  * Created by IntelliJ IDEA.
  * Author lihongbing on 5/22/2018.
  */

class Processor(dispatcher: Actor, assembler: Actor) extends Actor {
  var primeNumCount = 0

  //这个加工者找到的素数个数
  def act(): Unit = {
    dispatcher ! GIVE_ME_A_NEW_ONE //启动后就要一个数
    while (true) {
      receive {
        //如果没有更多数据了，就给分发者发一个I_AM_DONE
        case NO_MORE_NUMBERS =>
          assembler ! primeNumCount
          sender ! I_AM_DONE
          exit()
        //如果收到的消息是Int，就处理，处理完再要一个
        case number: Int =>
          if (isPrime(number)) {
            primeNumCount = primeNumCount + 1
          }
          sender ! GIVE_ME_A_NEW_ONE
      }
    }
  }

  //判读是否为素数
  def isPrime(num: Int): Boolean = {
    println("::::"+num)
    var flag = true
    for (i <- 2 to num - 1) {
      if (num % i == 0)
        flag = false
    }
    flag
  }

}
