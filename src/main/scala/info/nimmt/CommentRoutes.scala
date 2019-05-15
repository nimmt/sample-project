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

import info.nimmt.domain.model.Comment

trait CommentRoutes {
  // needed to run the route
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext = system.dispatcher

  implicit val commentFormat = jsonFormat2(Comment)

  // NOTE: 一時的な永続化領域
  var comments: List[Comment] = List(
    Comment("1", "hogehoge1"),
    Comment("2", "hogehoge2"),
    Comment("3", "hogehoge3")
  )

  def saveComment(comment: Comment): Future[Done] = {
    comments = comments :+ comment

    Future { Done }
  }

  lazy val commentRoutes: Route =
    path("comments") {
      get {
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
