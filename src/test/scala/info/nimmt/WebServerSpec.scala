import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, WordSpec }

import akka.http.scaladsl.server._
import Directives._

// class WebServerSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest with UserRoutes {

class WebServerSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest {

  // override val userRegistryActor: ActorRef =
  //   system.actorOf(UserRegistryActor.props, "userRegistry")

  // lazy val routes = userRoutes

  val routes =
    get {
      path("hello") {
        complete("PONG!")
      }
    }

  "WebServerRoutes" should {
    "return no users if no present (GET /hello)" in {
      val request = HttpRequest(uri = "/hello")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)
      }
    }
  }
}
