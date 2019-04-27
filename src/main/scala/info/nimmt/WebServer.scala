package info.nimmt

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object WebServer extends App with CommentRoutes {
  lazy val routes: Route = commentRoutes

  val f = Http().bindAndHandle(routes, "0.0.0.0", 8080)

  println(s"Started http://localhost:8080/comments")

  Await.ready(f, Duration.Inf)
}
