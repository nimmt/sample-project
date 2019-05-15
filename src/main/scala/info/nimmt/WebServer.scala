package info.nimmt

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object WebServer extends App with CommentRoutes {
  lazy val routes: Route = commentRoutes

  val bindingHandle = Http().bindAndHandle(routes, "0.0.0.0", 8080)

  Await.ready(bindingHandle, Duration.Inf)
}
