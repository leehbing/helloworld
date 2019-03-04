package com.actor.demo

/**
  * Created by IntelliJ IDEA.
  * Author lihongbing on 5/22/2018.
  */
//分发者告诉组装者完事了
case object STOP

//分发者告诉加工者没有原料了
case object NO_MORE_NUMBERS

//加工者告诉分发者我干完了
case object I_AM_DONE

//加工者告诉分发者再来一个
case object GIVE_ME_A_NEW_ONE


object ActorDemo {
  def main(args: Array[String]) {
    //组装actor启动，等待传过来找到的素数个数，最终组装结果
    val assembler = new Assembler
    assembler.start
    //原料分发actor启动，它需要知道组装者是谁，通知它停止组装并输出
    val dispatcher = new Dispatcher(1000, 10, assembler)
    dispatcher.start
    //启动10个加工actor，它需要从分发者那里取数，并把加工半成品给组装者
    for (i <- 0 to 10) {
      new Processor(dispatcher, assembler).start
    }
  }

}


