package router

import router.dto.TagDto
import service.TagService
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.routing.{ HttpService, Route }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

/**
 * Created by gneotux on 17/07/15.
 */
trait TagRouter extends HttpService with TagRouterDoc {
  self: Authenticator =>

  val tagService: TagService

  val tagOperations: Route = postRouteTag ~ readRouteTag ~ readAllRouteTag ~ deleteRouteTag ~ updateRouteTag

  override def readRouteTag = path("tags" / IntNumber) { tagId =>
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(tagService.get(tagId)) {
            case Success(Some(tag)) => complete(tag)
            case Success(None) => complete(NotFound, "Tag not found")
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def readAllRouteTag = path("tags") {
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(tagService.getAll) {
            case Success(tags) => complete(tags)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def deleteRouteTag = path("tags" / IntNumber) { tagId =>
    delete {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          respondWithMediaType(`application/json`) {
            onComplete(tagService.delete(tagId)) {
              case Success(ok) => complete(OK)
              case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }

  override def updateRouteTag: Route = path("tags" / IntNumber) { tagId =>
    put {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          entity(as[TagDto]) { tag =>
            respondWithMediaType(`application/json`) {
              onComplete(tagService.update(tagId, tag)) {
                case Success(Some(newTag)) => complete(OK, newTag)
                case Success(None) => complete(NotAcceptable, "Invalid tag")
                case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
              }
            }
          }
        }
      }
    }
  }

  override def postRouteTag: Route = path("tags") {
    post {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          entity(as[TagDto]) { tag =>
            respondWithMediaType(`application/json`) {
              onComplete(tagService.add(tag)) {
                case Success(Some(newTag)) => complete(Created, newTag)
                case Success(None) => complete(NotAcceptable, "Invalid tag")
                case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
              }
            }
          }
        }
      }
    }
  }

}
