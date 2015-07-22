package router

import dao._
import model._
import org.specs2.mock._
import org.specs2.mutable.Specification
import router.dto.LocationDto
import service.{ LocationService, UserService }
import spray.http.{ BasicHttpCredentials, StatusCodes }
import spray.httpx.SprayJsonSupport._
import spray.testkit.Specs2RouteTest
import utils.DatabaseConfig._
import utils.{ DatabaseSupportSpec, SpecSupport }

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class LocationIntegrationSpec extends Specification with Specs2RouteTest with LocationRouter with SpecSupport with Authenticator with Mockito {

  // connects the DSL to the test ActorSystem
  implicit def actorRefFactory = system

  override val locationService = LocationService
  override val userService = UserService

  val userAdmin = BasicHttpCredentials("test1@test.com", "password1")
  val userNotAdmin = BasicHttpCredentials("test2@test.com", "password1")

  "Locations Endpoint" should {
    "leave GET requests to other paths unhandled" in this {
      Get("/kermit") ~> addCredentials(userAdmin) ~> locationOperations ~>  check  {
        handled must beFalse
      }
    }
  }

  "Locations Endpoint#locations" should {
    "return a list of locations for GET requests to locations path" in this {
      Get("/locations") ~> addCredentials(userAdmin) ~> locationOperations ~> check {
        responseAs[Seq[Location]] === DatabaseSupportSpec.locations
      }
    }


    "return a single location for GET requests to locations path" in this {
      Get("/locations/1") ~> addCredentials(userAdmin) ~> locationOperations ~> check {
        responseAs[Location] === DatabaseSupportSpec.locations.filter(_.id == 1).head
      }
    }

    "return the id for DELETE requests to locations path" in this {
      Delete("/locations/1") ~> addCredentials(userAdmin) ~> locationOperations ~> check {
        val result: Seq[Location] = Await.result(db.run{LocationDao.getAll}, Duration.Inf)
        result mustEqual DatabaseSupportSpec.locations.filterNot(_.id == 1)
      }
    }

    "return the correct location for POST requests to locations path" in this {
      Post("/locations", LocationDto("Plaza toros leganes", Some("bull2"), 1231231111.22312D, 12312333123.123123D, 200, Some("Leganes space for acts"), Some("www.leganes.es"))

      ) ~> addCredentials(userAdmin) ~> locationOperations ~> check {
        status mustEqual StatusCodes.Created
        responseAs[Location] ===  Location(3,"Plaza toros leganes", Some("bull2"), 1231231111.22312D, 12312333123.123123D, 200, Some("Leganes space for acts"), Some("www.leganes.es"))
      }
    }

    //    "return not authorized for location without valid permission for DELETE requests to locations path" in this {
    //      Delete("/locations/1") ~> addCredentials(userNotAdmin) ~> locationOperations ~> check {
    //        status mustEqual StatusCodes.InternalServerError
    //      }
    //    }.isSkipped
    //
    //    "return not authorized for location without valid permission for POST requests to locations path" in this {
    //      Post("/locations", LocationDto("test4@test.com", Some("name4"), Some("surname4"), None, None, None, "USER", "NEWPASSWORD")) ~> addCredentials(userNotAdmin) ~> locationOperations ~> check {
    //        status mustEqual StatusCodes.InternalServerError
    //      }
    //    }.isSkipped
  }
}
