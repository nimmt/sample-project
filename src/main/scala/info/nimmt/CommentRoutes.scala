package info.nimmt

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

// NOTE: JsonSupport https://qiita.com/petitviolet/items/79f2bd3b4f1d54d38db1
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

// NOTE: ある時点において利用可能となる可能性のある値を保持するオブジェクト
// NOTE: https://docs.scala-lang.org/ja/overviews/core/futures.html
import scala.concurrent.Future

import info.nimmt.domain.model.Comment
import info.nimmt.domain.model.Thread

trait CommentRoutes {
  // needed to run the route
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext = system.dispatcher

  // NOTE: implicit: https://qiita.com/miyatin0212/items/f70cf68e89e4367fcf2e
  implicit val commentFormat = jsonFormat2(Comment)
  implicit val threadFormat = jsonFormat1(Thread)

  // NOTE: 一時的な永続化領域
  var threads: List[Comment] = List(
    Comment(1, "hogehoge1"),
    Comment(2, "hogehoge2"),
    Comment(3, "hogehoge3")
  )

  // NOTE: (fake) async database query api
  def fetchComment(id: Long): Future[Option[Comment]] = Future {
    threads.find(o => o.id == id)
  }

  def saveThread(thread: Thread): Future[Done] = {
    threads = thread match {
      case Thread(comments) => comments ::: threads
      case _                => threads
    }
    Future { Done }
  }

  lazy val commentRoutes: Route =
    path("comments") {
      get {
        complete(threads)
      } ~
      post {
        entity(as[Thread]) { thread =>
          val saved: Future[Done] = saveThread(thread)

          onComplete(saved) { done =>
            complete(thread)
          }
        }
      }
    }
}
