package router

import router.dto.EventDayDto
import service.EventDayService
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.routing.{ HttpService, Route }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

/**
 * Created by gneotux on 17/07/15.
 */
trait EventDayRouter extends HttpService with EventDayRouterDoc {
  self: Authenticator =>

  val eventDayService: EventDayService

  val eventDayOperations: Route = postRouteEventDay ~ readRouteEventDay ~ readAllRouteEventDay ~ deleteRouteEventDay

  override def readRouteEventDay = path("eventdays" / IntNumber) { eventDayId =>
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(eventDayService.get(eventDayId)) {
            case Success(Some(eventDay)) => complete(eventDay)
            case Success(None) => complete(NotFound, "EventDay not found")
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def readAllRouteEventDay = path("eventdays") {
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(eventDayService.getAll) {
            case Success(eventDays) => complete(eventDays)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def deleteRouteEventDay = path("eventdays" / IntNumber) { eventDayId =>
    delete {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          respondWithMediaType(`application/json`) {
            onComplete(eventDayService.delete(eventDayId)) {
              case Success(ok) => complete(OK)
              case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }

  override def postRouteEventDay: Route = path("eventdays") {
    post {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          entity(as[EventDayDto]) { eventDay =>
            respondWithMediaType(`application/json`) {
              onComplete(eventDayService.add(eventDay)) {
                case Success(Some(newEventDay)) => complete(Created, newEventDay)
                case Success(None) => complete(NotAcceptable, "Invalid eventDay")
                case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
              }
            }
          }
        }
      }
    }
  }

}
