package info.nimmt

import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import scala.util.{Failure, Success}

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future

import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

import info.nimmt.domain.model.comment.Comment
import info.nimmt.domain.model.comment.Comments

trait CommentRoutes {
  implicit val commentFormat = jsonFormat2(Comment)

  /** Database **/
  var db: Database = Database.forConfig("mysql")

  def saveComment(comment: Comment): Future[Done] = {
    db.run(Comments += comment)

    Future { Done }
  }

  lazy val commentRoutes: Route =
    path("comments") {
      get {
        // NOTE: 一時的な永続化領域
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
