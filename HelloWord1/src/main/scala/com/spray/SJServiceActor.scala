package com.spray

/**
  * Created by IntelliJ IDEA.
  * Author lihongbing on 8/4/2018.
  */

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import spray.httpx.SprayJsonSupport._
import MyJsonProtocol._

// 处理路由简单的actor
class SJServiceActor extends Actor with HttpService {
  // required as implicit value for the HttpService
  // included from SJService
  def actorRefFactory = context
  // 我们不自己创建接受函数，而是使用
  //HttpService的runRoute基于已经提供的路由来创建
  def receive = runRoute(aSimpleRoute ~ anotherRoute)
  // 一些路由案例
  // handles the api path, we could also define these in separate files
  // this path respons to get queries, and make a selection on the
  // media-type.
  val aSimpleRoute = {
    path("path1") {
      get {
        // 获得content-header值 Get the value of the content-header. Spray
        // provides multiple ways to do this.
        headerValue({
          case x@HttpHeaders.`Content-Type`(value) => Some(value)
          case default => None
        }) {
          // 包含内容类型的header被传递，我们来匹配判断一下
          header => header match {
            // 如果我们有这个内容类型，我们创建一个响应
            case ContentType(MediaType("application/vnd.type.a"), _) => {
              respondWithMediaType(`application/json`) {
                complete {
                  Person("Bob", "Type A", System.currentTimeMillis());
                }
              }
            }

            // 如果有其他内容类型 返回不同类型
            case ContentType(MediaType("application/vnd.type.b"), _) => {
              respondWithMediaType(`application/json`) {
                complete {
                  Person("Bob", "Type B", System.currentTimeMillis());
                }
              }
            }

            // 如果内容类型不匹配,返回错误编码
            case default => {
              complete {
                HttpResponse(406);
              }
            }
          }
        }
      }
    }
  }


  // 处理其他路由， we could also define these in separate files

  // This is just a simple route to explain the concept

  val anotherRoute = {
    path("path2") {
      get {
        // respond with text/html.
        respondWithMediaType(`text/html`) {
          complete {
            // respond with a set of HTML elements
            <html>
              <body>
                <h1>Path 2</h1>
              </body>
            </html>
          }
        }
      }
    }
  }
}