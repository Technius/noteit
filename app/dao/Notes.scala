package dao

import javax.inject.{ Inject, Singleton }
import io.getquill._
import io.getquill.naming.SnakeCase
import io.getquill.sources.sql.idiom.PostgresDialect
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import models.Note

@Singleton
class NotesDao {
  lazy val db = source(new PostgresAsyncSourceConfig[SnakeCase]("db.default"))

  private val notes = quote {
    query[Note].schema(_.entity("notes"))
  }

  private val byId = quote { (id: String) =>
    notes.filter(_.id == id)
  }

  def insert(note: Note): Future[Long] = db.run(notes.insert)(note)

  def delete(id: String): Future[Long] = db.run {
    quote(byId(lift(id)).delete)
  }

  def find(id: String): Future[Option[Note]] = db.run {
    quote {
      byId(lift(id)).take(1)
    }
  }.map(_.headOption)

  def update(id: String, contents: String): Future[Long] = db.run {
    quote {
      byId(lift(id)).update(_.contents -> lift(contents))
    }
  }
}
