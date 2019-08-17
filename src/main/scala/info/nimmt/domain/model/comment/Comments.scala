package info.nimmt.domain.model.comment

import slick.driver.MySQLDriver.api._

import info.nimmt.domain.model.comment.Comment

class Comments(tag: Tag) extends Table[Comment](tag, "comments") {
  def id = column[Long]("id", O.PrimaryKey)
  def content = column[String]("content")
  def contributor_name = column[String]("contributor_name")

  def * = (content, contributor_name) <> (Comment.tupled, Comment.unapply)
}

object Comments extends TableQuery(new Comments(_))
