package com.actor.demo

import scala.actors.Actor

/**
  * Created by IntelliJ IDEA.
  * Author lihongbing on 5/22/2018.
  */

class Dispatcher(maxNum: Int, actorCount: Int, assembler: Actor) extends Actor {
  var currentNum = 2   //从2开始分发
  var actors = actorCount //加工者的个数，当所有这些加工者都发了I_AM_DONE后，actorCount为0
  def act(): Unit = {
    while (true) {
      receive {
        /*收到GIVE_ME_A_NEW_ONE,检查是否已经用完了，
        没用完继续给，用完了则发NO_MORE_NUMBERS   */
        case GIVE_ME_A_NEW_ONE =>
          if (currentNum <= maxNum) {
            sender ! currentNum
            currentNum = currentNum + 1
          } else {
            sender ! NO_MORE_NUMBERS
          }
        /*收到I_AM_DONE,说明一个Processor干完了
        actors-1，为0则说明组装者可以完成组装了 */
        case I_AM_DONE =>
          actors = actors - 1
          println("xxx:"+actors)
          if (actors == 0) {
            assembler ! STOP
          }
          exit()
      }
    }
  }
}
