package info.nimmt.domain.model.comment

import slick.driver.MySQLDriver.api._

import info.nimmt.domain.model.comment.Comment

class Comments(tag: Tag) extends Table[Comment](tag, "comments") {
  def identity = column[String]("identity", O.PrimaryKey)
  def content = column[String]("content")

  def * = (identity, content) <> (Comment.tupled, Comment.unapply)
}

object Comments extends TableQuery(new Comments(_))
