package router

import router.dto.ActivityTypeDto
import service.ActivityTypeService
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.routing.{ HttpService, Route }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

/**
 * Created by gneotux on 17/07/15.
 */
trait ActivityTypeRouter extends HttpService with ActivityTypeRouterDoc {
  self: Authenticator =>

  val activityTypeService: ActivityTypeService

  val activityTypeOperations: Route = postRouteActivityType ~ readRouteActivityType ~ readAllRouteActivityType ~ deleteRouteActivityType

  override def readRouteActivityType = path("activitytypes" / IntNumber) { activityTypeId =>
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(activityTypeService.get(activityTypeId)) {
            case Success(Some(activityType)) => complete(activityType)
            case Success(None) => complete(NotFound, "ActivityType not found")
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def readAllRouteActivityType = path("activitytypes") {
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(activityTypeService.getAll) {
            case Success(activityTypes) => complete(activityTypes)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def deleteRouteActivityType = path("activitytypes" / IntNumber) { activityTypeId =>
    delete {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          respondWithMediaType(`application/json`) {
            onComplete(activityTypeService.delete(activityTypeId)) {
              case Success(ok) => complete(OK)
              case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }

  override def postRouteActivityType: Route = path("activitytypes") {
    post {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          entity(as[ActivityTypeDto]) { activityType =>
            respondWithMediaType(`application/json`) {
              onComplete(activityTypeService.add(activityType)) {
                case Success(Some(newActivityType)) => complete(Created, newActivityType)
                case Success(None) => complete(NotAcceptable, "Invalid activityType")
                case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
              }
            }
          }
        }
      }
    }
  }

}
