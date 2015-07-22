package router

import dao._
import model._
import org.specs2.mock._
import org.specs2.mutable.Specification
import router.dto.ActivityTypeDto
import service.{ ActivityTypeService, UserService }
import spray.http.{ BasicHttpCredentials, StatusCodes }
import spray.httpx.SprayJsonSupport._
import spray.testkit.Specs2RouteTest
import utils.DatabaseConfig._
import utils.{ DatabaseSupportSpec, SpecSupport }

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class ActivityTypeIntegrationSpec extends Specification with Specs2RouteTest with ActivityTypeRouter with SpecSupport with Authenticator with Mockito {

  // connects the DSL to the test ActorSystem
  implicit def actorRefFactory = system

  override val activityTypeService = ActivityTypeService
  override val userService = UserService

  val userAdmin = BasicHttpCredentials("test1@test.com", "password1")
  val userNotAdmin = BasicHttpCredentials("test2@test.com", "password1")

  "ActivityTypes Endpoint" should {
    "leave GET requests to other paths unhandled" in this {
      Get("/kermit") ~> addCredentials(userAdmin) ~> activityTypeOperations ~>  check  {
        handled must beFalse
      }
    }
  }

  "ActivityTypes Endpoint#activityTypes" should {
    "return a list of activityTypes for GET requests to activityTypes path" in this {
      Get("/activitytypes") ~> addCredentials(userAdmin) ~> activityTypeOperations ~> check {
        responseAs[Seq[ActivityType]] === DatabaseSupportSpec.activityTypes
      }
    }


    "return a single activityType for GET requests to activityTypes path" in this {
      Get("/activitytypes/1") ~> addCredentials(userAdmin) ~> activityTypeOperations ~> check {
        responseAs[ActivityType] === DatabaseSupportSpec.activityTypes.filter(_.id == 1).head
      }
    }

    "return the id for DELETE requests to activityTypes path" in this {
      Delete("/activitytypes/1") ~> addCredentials(userAdmin) ~> activityTypeOperations ~> check {
        val result: Seq[ActivityType] = Await.result(db.run{ActivityTypeDao.getAll}, Duration.Inf)
        result mustEqual DatabaseSupportSpec.activityTypes.filterNot(_.id == 1)
      }
    }

    "return the correct activityType for POST requests to activityTypes path" in this {
      Post("/activitytypes", ActivityTypeDto("newType")
      ) ~> addCredentials(userAdmin) ~> activityTypeOperations ~> check {
        status mustEqual StatusCodes.Created
        responseAs[ActivityType] ===  ActivityType(6, "newType")
      }
    }

    //    "return not authorized for activityType without valid permission for DELETE requests to activityTypes path" in this {
    //      Delete("/activitytypes/1") ~> addCredentials(userNotAdmin) ~> activityTypeOperations ~> check {
    //        status mustEqual StatusCodes.InternalServerError
    //      }
    //    }.isSkipped
    //
    //    "return not authorized for activityType without valid permission for POST requests to activityTypes path" in this {
    //      Post("/activitytypes", ActivityTypeDto("test4@test.com", Some("name4"), Some("surname4"), None, None, None, "USER", "NEWPASSWORD")) ~> addCredentials(userNotAdmin) ~> activityTypeOperations ~> check {
    //        status mustEqual StatusCodes.InternalServerError
    //      }
    //    }.isSkipped
  }
}
