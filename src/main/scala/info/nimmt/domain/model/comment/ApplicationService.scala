package info.nimmt.domain.model.comment

import akka.actor.{ Actor, ActorSystem, Props }

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import info.nimmt.domain.model.comment.Comments
import info.nimmt.connection.MySQLDBImpl

object ApplicationService {
  def props: Props = Props[ApplicationService]
  final case class Create(comment: Comment)
}

class ApplicationService extends Actor with MySQLDBImpl {
  import ApplicationService._
  import driver.api._

  def receive = {
    case Create(comment) =>
      Await.result(
        db.run(Comments += comment),
        Duration.Inf
      )
  }
}
