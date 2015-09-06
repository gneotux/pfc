package router

import dao._
import model._
import org.specs2.mock._
import org.specs2.mutable.Specification
import router.dto.EventDto
import service.{ EventService, UserService }
import spray.http.{ BasicHttpCredentials, StatusCodes }
import spray.httpx.SprayJsonSupport._
import spray.testkit.Specs2RouteTest
import utils.DatabaseConfig._
import utils.{ DatabaseSupportSpec, SpecSupport }

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class EventIntegrationSpec extends Specification with Specs2RouteTest with EventRouter with SpecSupport with Authenticator with Mockito {

  // connects the DSL to the test ActorSystem
  implicit def actorRefFactory = system

  override val eventService = EventService
  override val userService = UserService

  val userAdmin = BasicHttpCredentials("test1@test.com", "password1")
  val userNotAdmin = BasicHttpCredentials("test2@test.com", "password1")

  "Events Endpoint" should {
    "leave GET requests to other paths unhandled" in this {
      Get("/kermit") ~> addCredentials(userAdmin) ~> eventOperations ~>  check  {
        handled must beFalse
      }
    }
  }

  "Events Endpoint#events" should {
    "return a list of events for GET requests to events path" in this {
      Get("/events") ~> addCredentials(userAdmin) ~> eventOperations ~> check {
        responseAs[Seq[Event]] === DatabaseSupportSpec.events
      }
    }


    "return a single event for GET requests to events path" in this {
      Get("/events/1") ~> addCredentials(userAdmin) ~> eventOperations ~> check {
        responseAs[Event] === DatabaseSupportSpec.events.filter(_.id == 1).head
      }
    }

    "return the id for DELETE requests to events path" in this {
      Delete("/events/1") ~> addCredentials(userAdmin) ~> eventOperations ~> check {
        val result: Seq[Event] = Await.result(db.run{EventDao.getAll}, Duration.Inf)
        result mustEqual DatabaseSupportSpec.events.filterNot(_.id == 1)
      }
    }

    "return the correct event for POST requests to events path" in this {
      Post("/events", EventDto("ScalaDays return", Some("The most third important event in the scala world"), Some("www.scaladaysreturn.com"), Some("#scaladays"), Some("http://scaladays.jpg"))
      ) ~> addCredentials(userAdmin) ~> eventOperations ~> check {
        status mustEqual StatusCodes.Created
        responseAs[Event] ===  Event(4, "ScalaDays return", Some("The most third important event in the scala world"), Some("www.scaladaysreturn.com"), Some("#scaladays"), Some("http://scaladays.jpg"))
      }
    }

    "return the correct event for PUT requests to events path" in this {
      Put("/events/1", EventDto("ScalaDays return2", Some("The most third important event in the scala world"), Some("www.scaladaysreturn.com"), Some("#scaladays"), Some("http://scaladays.jpg"))
      ) ~> addCredentials(userAdmin) ~> eventOperations ~> check {
        status mustEqual StatusCodes.OK
        responseAs[Event] ===  Event(1, "ScalaDays return2", Some("The most third important event in the scala world"), Some("www.scaladaysreturn.com"), Some("#scaladays"), Some("http://scaladays.jpg"))
      }
    }

    //    "return not authorized for event without valid permission for DELETE requests to events path" in this {
    //      Delete("/events/1") ~> addCredentials(userNotAdmin) ~> eventOperations ~> check {
    //        status mustEqual StatusCodes.InternalServerError
    //      }
    //    }.isSkipped
    //
    //    "return not authorized for event without valid permission for POST requests to events path" in this {
    //      Post("/events", EventDto("test4@test.com", Some("name4"), Some("surname4"), None, None, None, "USER", "NEWPASSWORD")) ~> addCredentials(userNotAdmin) ~> eventOperations ~> check {
    //        status mustEqual StatusCodes.InternalServerError
    //      }
    //    }.isSkipped
  }
}
