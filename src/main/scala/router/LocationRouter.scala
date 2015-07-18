package router

import router.dto.LocationDto
import service.LocationService
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.routing.{ HttpService, Route }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

/**
 * Created by gneotux on 18/07/15.
 */
trait LocationRouter extends HttpService with LocationRouterDoc {
  self: Authenticator =>

  val locationService: LocationService

  val locationOperations: Route = postRouteLocation ~ readRouteLocation ~ readAllRouteLocation ~ deleteRouteLocation

  override def readRouteLocation = path("locations" / IntNumber) { locationId =>
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(locationService.get(locationId)) {
            case Success(Some(location)) => complete(location)
            case Success(None) => complete(NotFound, "Location not found")
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def readAllRouteLocation = path("locations") {
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(locationService.getAll) {
            case Success(locations) => complete(locations)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def deleteRouteLocation = path("locations" / IntNumber) { locationId =>
    delete {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(locationService.delete(locationId)) {
            case Success(ok) => complete(OK)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def postRouteLocation: Route = path("locations") {
    post {
      authenticate(basicUserAuthenticator) { authInfo =>
        entity(as[LocationDto]) { location =>
          respondWithMediaType(`application/json`) {
            onComplete(locationService.add(location)) {
              case Success(Some(newLocation)) => complete(Created, newLocation)
              case Success(None) => complete(NotAcceptable, "Invalid location")
              case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }

}