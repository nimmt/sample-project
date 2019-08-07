package info.nimmt

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object WebServer extends CommentRoutes {
  // needed to run the route
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext = system.dispatcher

  def main(args: Array[String]) {
    lazy val routes: Route = commentRoutes

    val bindingHandle = Http().bindAndHandle(routes, "0.0.0.0", 8080)

    Await.ready(bindingHandle, Duration.Inf)
  }
}
