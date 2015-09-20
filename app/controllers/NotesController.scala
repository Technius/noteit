package controllers

import javax.inject.Inject
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import dao.NotesDao
import models.Note

class NotesController @Inject()(notes: NotesDao, val messagesApi: MessagesApi)
    extends Controller with I18nSupport {

  val uploadForm = Form(
    single("contents" -> nonEmptyText)
  )

  /**
   * Handles the upload form page.
   */
  def uploadPage = Action {
    Ok(views.html.upload(uploadForm.fill("")))
  }

  /**
   * Handles note uploads.
   */
  def upload = Action.async { implicit request =>
    uploadForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.upload(errorForm)))
      },
      contents => {
        val id = java.util.UUID.randomUUID().toString
        notes.insert(Note(id, contents)) map { _ =>
          Redirect(routes.NotesController.view(id))
        }
      }
    )
  }

  /**
   * Handles note views.
   */
  def view(id: String) = Action.async {
    notes.find(id) map {
      case Some(note) => Ok(views.html.view(note))
      case None => notFound
    }
  }

  /**
   * Handles the edit form page.
   */
  def editPage(id: String) = Action.async {
    notes.find(id) map {
      case Some(note) =>
        Ok(views.html.edit(id, uploadForm.fill(note.contents)))
      case None =>
        notFound
    }
  }

  /**
   * Handles edit form submissions.
   */
  def edit(id: String) = Action.async { implicit request =>
    uploadForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.edit(id, errorForm)))
      },
      contents => {
        notes.update(id, contents) map {
          case 0 => notFound // fix not redirecting
          case x => Redirect(routes.NotesController.view(id))
        }
      }
    )
  }

  /**
   * Helper for page not found
   */
  @inline def notFound = NotFound(views.html.upload(uploadForm.fill("")))
}
