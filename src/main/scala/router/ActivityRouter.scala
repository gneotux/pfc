package router

import router.dto.ActivityDto
import service.ActivityService
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.routing.{ HttpService, Route }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

/**
 * Created by gneotux on 17/07/15.
 */
trait ActivityRouter extends HttpService with ActivityRouterDoc {
  self: Authenticator =>

  val activityService: ActivityService

  val activityOperations: Route = postRouteActivity ~ readRouteActivity ~ readAllRouteActivity ~ deleteRouteActivity

  override def readRouteActivity = path("activities" / IntNumber) { activityId =>
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(activityService.get(activityId)) {
            case Success(Some(activity)) => complete(activity)
            case Success(None) => complete(NotFound, "Activity not found")
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def readAllRouteActivity = path("activities") {
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(activityService.getAll) {
            case Success(activities) => complete(activities)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def readAllAtendeesInActivity = path("activities" / IntNumber / "atendees") { activityId =>
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(activityService.getAllAtendees(activityId)) {
            case Success(atendees) => complete(atendees)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def readAllSpeakersInActivity = path("activities" / IntNumber / "speakers") { activityId =>
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(activityService.getAllSpeakers(activityId)) {
            case Success(speakers) => complete(speakers)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def deleteRouteActivity = path("activities" / IntNumber) { activityId =>
    delete {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(activityService.delete(activityId)) {
            case Success(ok) => complete(OK)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def postRouteActivity: Route = path("activities") {
    post {
      authenticate(basicUserAuthenticator) { authInfo =>
        entity(as[ActivityDto]) { activity =>
          respondWithMediaType(`application/json`) {
            onComplete(activityService.add(activity)) {
              case Success(Some(newActivity)) => complete(Created, newActivity)
              case Success(None) => complete(NotAcceptable, "Invalid activity")
              case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }

  def postRouteActivityAtendee: Route = path("activities" / IntNumber / "atendees" / IntNumber) { (activityId, userId) =>
    post {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(activityService.addAtendee(activityId, userId)) {
            case Success(atendee) => complete(atendee)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  def postRouteActivitySpeaker: Route = path("activities" / IntNumber / "speakers"/ IntNumber) { (activityId, userId) =>
    post {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(activityService.addSpeaker(activityId, userId)) {
            case Success(speaker) => complete(speaker)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }


}
