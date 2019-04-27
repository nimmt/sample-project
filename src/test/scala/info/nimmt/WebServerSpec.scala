import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, WordSpec }

import akka.http.scaladsl.server._
import Directives._

import info.nimmt.CommentRoutes

class WebServerSpec
  extends WordSpec
  with Matchers
  with ScalaFutures
  with ScalatestRouteTest
  with CommentRoutes
{

  lazy val routes: Route = commentRoutes

  "WebServerRoutes" should {
    "return (GET /comments)" in {
      val request = HttpRequest(uri = "/comments")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        contentType should ===(ContentTypes.`application/json`)
      }
    }
  }
}
