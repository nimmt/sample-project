package info.nimmt

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future

import akka.actor._
import akka.persistence._

trait CommentRoutes {
  // needed to run the route
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext = system.dispatcher

  // test start
  val persistentActor = system.actorOf(Props[CommentPersistentActor], "persistentActor-4-scala")
  // test end

  // NOTE: implicit: https://qiita.com/miyatin0212/items/f70cf68e89e4367fcf2e
  implicit val commentFormat = jsonFormat2(Comment)

  def saveComment(comment: Comment): Future[Done] = {
    persistentActor ! comment

    Future { Done }
  }

  lazy val commentRoutes: Route =
    path("comments") {
      get {
        var comments: List[Comment] = List(
          Comment("1", "hogehoge1"),
          Comment("2", "hogehoge2"),
          Comment("3", "hogehoge3")
        )

        complete(comments)
      } ~
      post {
        entity(as[Comment]) { comment =>
          val saved: Future[Done] = saveComment(comment)

          onComplete(saved) { done =>
            complete(comment)
          }
        }
      }
    }
}
