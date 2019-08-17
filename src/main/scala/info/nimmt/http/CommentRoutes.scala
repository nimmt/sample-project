package info.nimmt.http

import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import akka.stream.ActorMaterializer

import akka.actor.{ ActorSystem, ActorRef }

import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext.Implicits.global

import info.nimmt.domain.model.comment.{ Comment, ApplicationService }

trait CommentRoutes {
  // needed to run the route
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext = system.dispatcher

  implicit val commentFormat = jsonFormat2(Comment)

  lazy val commentRoutes: Route = {
    val service: ActorRef = system.actorOf(ApplicationService.props, "service")

    path("comments") {
      get {
        // NOTE: 一時的な永続化領域
        var comments: List[Comment] = List(
          Comment("hogehoge1", "inoue"),
          Comment("hogehoge2", "inoue"),
          Comment("hogehoge3", "inoue")
        )

        complete(comments)
      } ~
      post {
        entity(as[Comment]) { comment =>
          service ! comment

          complete(comment)
        }
      }
    }
  }
}
