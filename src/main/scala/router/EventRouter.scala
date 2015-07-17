package router

import router.dto.EventDto
import service.EventService
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.routing.{ HttpService, Route }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

/**
 * Created by gneotux on 17/07/15.
 */
trait EventRouter extends HttpService with EventRouterDoc {
  self: Authenticator =>

  val eventService: EventService

  val eventOperations: Route = postRouteEvent ~ readRouteEvent ~ readAllRouteEvent ~ deleteRouteEvent

  override def readRouteEvent = path("events" / IntNumber) { eventId =>
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(eventService.get(eventId)) {
            case Success(Some(event)) => complete(event)
            case Success(None) => complete(NotFound, "Event not found")
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def readAllRouteEvent = path("events") {
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(eventService.getAll) {
            case Success(events) => complete(events)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def deleteRouteEvent = path("events" / IntNumber) { eventId =>
    delete {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(eventService.delete(eventId)) {
            case Success(ok) => complete(OK)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def postRouteEvent: Route = path("events") {
    post {
      authenticate(basicUserAuthenticator) { authInfo =>
        entity(as[EventDto]) { event =>
          respondWithMediaType(`application/json`) {
            onComplete(eventService.add(event)) {
              case Success(Some(newEvent)) => complete(Created, newEvent)
              case Success(None) => complete(NotAcceptable, "Invalid event")
              case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }

}
