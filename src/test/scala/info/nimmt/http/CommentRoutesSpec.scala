import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, WordSpec }

import akka.http.scaladsl.server._
import Directives._

import info.nimmt.http.CommentRoutes
import info.nimmt.domain.model.comment.Comment

class CommentRoutesSpec
  extends WordSpec
  with Matchers
  with ScalaFutures
  with ScalatestRouteTest
  with CommentRoutes
{

  // https://developer.lightbend.com/guides/akka-http-quickstart-scala/testing-routes.html
  lazy val routes: Route = commentRoutes

  "CommetRoutes" should {
    "be able to add comments (POST /comments)" in {
      val comment = Comment("コメントだよ", "ino_h")
      // https://github.com/akka/akka-http/blob/f57ec755a9107a8c1c25412ecde3149a91261e7c/akka-http-marshallers-scala/akka-http-spray-json/src/test/scala/akka/http/scaladsl/marshallers/sprayjson/SprayJsonSupportSpec.scala
      val commentEntity = Marshal(comment).to[MessageEntity].futureValue

      // using the RequestBuilding DSL:
      val request = Post("/comments").withEntity(commentEntity)

      request ~> routes ~> check {
        status should ===(StatusCodes.Created)

        contentType should ===(ContentTypes.`application/json`)
      }
    }
  }
}
