package router

import dao._
import model._
import org.specs2.mock._
import org.specs2.mutable.Specification
import router.dto.EventDayDto
import service.{ EventDayService, UserService }
import spray.http.{ BasicHttpCredentials, StatusCodes }
import spray.httpx.SprayJsonSupport._
import spray.testkit.Specs2RouteTest
import utils.DatabaseConfig._
import utils.{ DatabaseSupportSpec, SpecSupport }

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class EventDayIntegrationSpec extends Specification with Specs2RouteTest with EventDayRouter with SpecSupport with Authenticator with Mockito {

  // connects the DSL to the test ActorSystem
  implicit def actorRefFactory = system

  override val eventDayService = EventDayService
  override val userService = UserService

  val userAdmin = BasicHttpCredentials("test1@test.com", "password1")
  val userNotAdmin = BasicHttpCredentials("test2@test.com", "password1")

  "EventDays Endpoint" should {
    "leave GET requests to other paths unhandled" in this {
      Get("/kermit") ~> addCredentials(userAdmin) ~> eventDayOperations ~>  check  {
        handled must beFalse
      }
    }
  }

  "EventDays Endpoint#eventDays" should {
    "return a list of eventDays for GET requests to eventDays path" in this {
      Get("/eventdays") ~> addCredentials(userAdmin) ~> eventDayOperations ~> check {
        responseAs[Seq[EventDay]] === DatabaseSupportSpec.eventDays
      }
    }


    "return a single eventDay for GET requests to eventDays path" in this {
      Get("/eventdays/1") ~> addCredentials(userAdmin) ~> eventDayOperations ~> check {
        responseAs[EventDay] === DatabaseSupportSpec.eventDays.filter(_.id == 1).head
      }
    }

    "return the id for DELETE requests to eventDays path" in this {
      Delete("/eventdays/1") ~> addCredentials(userAdmin) ~> eventDayOperations ~> check {
        val result: Seq[EventDay] = Await.result(db.run{EventDayDao.getAll}, Duration.Inf)
        result mustEqual DatabaseSupportSpec.eventDays.filterNot(_.id == 1)
      }
    }

    "return the correct eventDay for POST requests to eventDays path" in this {
      Post("/eventdays", EventDayDto(1, DatabaseSupportSpec.now, DatabaseSupportSpec.now)
      ) ~> addCredentials(userAdmin) ~> eventDayOperations ~> check {
        status mustEqual StatusCodes.Created
        responseAs[EventDay] ===  EventDay(10, 1, DatabaseSupportSpec.now, DatabaseSupportSpec.now)
      }
    }

    //    "return not authorized for eventDay without valid permission for DELETE requests to eventDays path" in this {
    //      Delete("/eventdays/1") ~> addCredentials(userNotAdmin) ~> eventDayOperations ~> check {
    //        status mustEqual StatusCodes.InternalServerError
    //      }
    //    }.isSkipped
    //
    //    "return not authorized for eventDay without valid permission for POST requests to eventDays path" in this {
    //      Post("/eventdays", EventDayDto("test4@test.com", Some("name4"), Some("surname4"), None, None, None, "USER", "NEWPASSWORD")) ~> addCredentials(userNotAdmin) ~> eventDayOperations ~> check {
    //        status mustEqual StatusCodes.InternalServerError
    //      }
    //    }.isSkipped
  }
}
