package router

import router.dto.CompanyDto
import service.CompanyService
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.routing.{ HttpService, Route }
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global


// this trait defines our service behavior independently from the service actor
trait CompanyRouter extends HttpService with CompanyRouterDoc {
  self: Authenticator =>

  val companyService: CompanyService

  val companyOperations: Route = postRouteCompany ~ readRouteCompany ~ readAllRouteCompany ~ deleteRouteCompany ~ updateRouteCompany

  override def readRouteCompany = path("companies" / IntNumber) { companyId =>
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(companyService.get(companyId)) {
            case Success(Some(company)) => complete(company)
            case Success(None) => complete(NotFound, "Company not found")
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def readAllRouteCompany = path("companies") {
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(companyService.getAll) {
            case Success(companies) => complete(companies)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def deleteRouteCompany = path("companies" / IntNumber) { companyId =>
    delete {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          respondWithMediaType(`application/json`) {
            onComplete(companyService.delete(companyId)) {
              case Success(ok) => complete(OK)
              case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }

  override def updateRouteCompany: Route = path("companies" / IntNumber) { companyId =>
    put {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          entity(as[CompanyDto]) { company =>
            respondWithMediaType(`application/json`) {
              onComplete(companyService.update(companyId, company)) {
                case Success(Some(newCompany)) => complete(OK, newCompany)
                case Success(None) => complete(NotAcceptable, "Invalid company")
                case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
              }
            }
          }
        }
      }
    }
  }

  override def postRouteCompany: Route = path("companies") {
    post {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          entity(as[CompanyDto]) { company =>
            respondWithMediaType(`application/json`) {
              onComplete(companyService.add(company)) {
                case Success(Some(newCompany)) => complete(Created, newCompany)
                case Success(None) => complete(NotAcceptable, "Invalid company")
                case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
              }
            }
          }
        }
      }
    }
  }

}
