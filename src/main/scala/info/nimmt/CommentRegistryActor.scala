// package info.nimmt

// import akka.actor.{ Actor, ActorLogging, Props }

// final case class Comments(users: Seq[Comment])

// object CommentRegistryActor {
//   final case class ActionPerformed(description: String)
//   final case object GetComments
//   final case class CreateComment(comment: Comment)
//   final case class GetComment(identity: String)
//   final case class DeleteComment(identity: String)

//   def props: Props = Props[CommentRegistryActor]
// }

// class CommentRegistryActor extends Actor with ActorLogging {
//   import CommentRegistryActor._

//   var comments = Set.empty[Comment]

//   def receive: Receive = {
//     case GetComments =>
//       sender() ! Comments(comments.toSeq)
//     case CreateComment(comment) =>
//       comments += comment
//       sender() ! ActionPerformed(s"Comment \${comment.identity} created.")
//     case GetComment(identity) =>
//       sender() ! comments.find(_.identity == identity)
//     case DeleteComment(identity) =>
//       comments.find(_.identity == identity) foreach { comment => comments -= comment }
//       sender() ! ActionPerformed(s"Comment \${identity} deleted.")
//   }
// }
