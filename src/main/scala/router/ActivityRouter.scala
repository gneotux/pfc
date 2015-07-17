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

}
