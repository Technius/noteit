package dao

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.driver.JdbcProfile
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import models.Note

trait NotesComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import driver.api._

  class Notes(tag: Tag) extends Table[Note](tag, "notes") {
    def id = column[String]("id", O.PrimaryKey)
    def contents = column[String]("contents")
    def * = (id, contents) <> (Note.tupled, Note.unapply _)
  }
}

@Singleton
class NotesDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
    extends NotesComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  val notes = TableQuery[Notes]

  def insert(note: Note): Future[Unit] = db.run(notes += note).map(_ => ())

  def delete(id: String): Future[Int] =
    db.run(notes.filter(_.id === id).delete)

  def find(id: String): Future[Option[Note]] =
    db.run(notes.filter(_.id === id).take(1).result).map(_.headOption)

  def update(id: String, contents: String): Future[Int] =
    db.run(notes.filter(_.id === id).map(_.contents).update(contents))
}
