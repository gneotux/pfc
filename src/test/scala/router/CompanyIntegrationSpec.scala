package router

import dao._
import model._
import org.specs2.mock._
import org.specs2.mutable.Specification
import router.dto.CompanyDto
import service.{ CompanyService, UserService }
import spray.http.{ BasicHttpCredentials, StatusCodes }
import spray.httpx.SprayJsonSupport._
import spray.testkit.Specs2RouteTest
import utils.DatabaseConfig._
import utils.{ DatabaseSupportSpec, SpecSupport }

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class CompanyIntegrationSpec extends Specification with Specs2RouteTest with CompanyRouter with SpecSupport with Authenticator with Mockito {

  // connects the DSL to the test ActorSystem
  implicit def actorRefFactory = system

  override val companyService = CompanyService
  override val userService = UserService

  val userAdmin = BasicHttpCredentials("test1@test.com", "password1")
  val userNotAdmin = BasicHttpCredentials("test2@test.com", "password1")

  "Companies Endpoint" should {
    "leave GET requests to other paths unhandled" in this {
      Get("/kermit") ~> addCredentials(userAdmin) ~> companyOperations ~>  check  {
        handled must beFalse
      }
    }
  }

  "Companies Endpoint#companies" should {
    "return a list of companies for GET requests to companies path" in this {
      Get("/companies") ~> addCredentials(userAdmin) ~> companyOperations ~> check {
        responseAs[Seq[Company]] === DatabaseSupportSpec.companies
      }
    }


    "return a single company for GET requests to companies path" in this {
      Get("/companies/1") ~> addCredentials(userAdmin) ~> companyOperations ~> check {
        responseAs[Company] === DatabaseSupportSpec.companies.filter(_.id == 1).head
      }
    }

    "return the id for DELETE requests to companies path" in this {
      Delete("/companies/1") ~> addCredentials(userAdmin) ~> companyOperations ~> check {
        val result: Seq[Company] = Await.result(db.run{CompanyDao.getAll}, Duration.Inf)
        result mustEqual DatabaseSupportSpec.companies.filterNot(_.id == 1)
      }
    }

    "return the correct company for POST requests to companies path" in this {
      Post("/companies", CompanyDto("emailofthenewcompany@Company")
      ) ~> addCredentials(userAdmin) ~> companyOperations ~> check {
        status mustEqual StatusCodes.Created
        responseAs[Company] ===  Company(3, "emailofthenewcompany@Company")
      }
    }

    //    "return not authorized for company without valid permission for DELETE requests to companies path" in this {
    //      Delete("/companies/1") ~> addCredentials(userNotAdmin) ~> companyOperations ~> check {
    //        status mustEqual StatusCodes.InternalServerError
    //      }
    //    }.isSkipped
    //
    //    "return not authorized for company without valid permission for POST requests to companies path" in this {
    //      Post("/companies", CompanyDto("test4@test.com", Some("name4"), Some("surname4"), None, None, None, "USER", "NEWPASSWORD")) ~> addCredentials(userNotAdmin) ~> companyOperations ~> check {
    //        status mustEqual StatusCodes.InternalServerError
    //      }
    //    }.isSkipped
  }
}
