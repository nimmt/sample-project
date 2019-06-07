package info.nimmt

import akka.actor._
import akka.persistence._

case class Evt(data: String)
final case class Comment(identity: String, content: String)

case class CommentState(events: List[String] = Nil) {
  def updated(evt: Evt): CommentState = copy(evt.data :: events)
  def size: Int = events.length
  override def toString: String = events.reverse.toString
}

class CommentPersistentActor extends PersistentActor {
  override def persistenceId = "sample-id-1"

  var state = CommentState()

  def updateState(event: Evt): Unit =
    state = state.updated(event)

  def numEvents =
    state.size

  val receiveRecover: Receive = {
    case evt: Evt                                 => updateState(evt)
    case SnapshotOffer(_, snapshot: CommentState) => state = snapshot
  }

  val receiveCommand: Receive = {
    case comment: Comment =>
      println(comment)
      persist(Evt(s"${comment.identity}-${numEvents}")) { event =>
        updateState(event)
        context.system.eventStream.publish(event)
      }
    case "snap"  => saveSnapshot(state)
    case "print" => println(state)
  }
}
