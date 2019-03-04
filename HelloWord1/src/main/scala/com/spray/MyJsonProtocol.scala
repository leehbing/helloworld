package com.spray

/**
  * Created by IntelliJ IDEA.
  * Author lihongbing on 8/4/2018.
  */
import spray.json.DefaultJsonProtocol
object MyJsonProtocol extends DefaultJsonProtocol {

  implicit val personFormat = jsonFormat3(Person)

}



case class Person(name: String, fistName: String, age: Long)
