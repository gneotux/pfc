package router

import dao._
import model._
import org.specs2.mock._
import org.specs2.mutable.Specification
import router.dto.ActivityDto
import service.{ ActivityService, UserService }
import spray.http.{ BasicHttpCredentials, StatusCodes }
import spray.httpx.SprayJsonSupport._
import spray.testkit.Specs2RouteTest
import utils.DatabaseConfig._
import utils.{ DatabaseSupportSpec, SpecSupport }

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class ActivityIntegrationSpec extends Specification with Specs2RouteTest with ActivityRouter with SpecSupport with Authenticator with Mockito {

  // connects the DSL to the test ActorSystem
  implicit def actorRefFactory = system

  override val activityService = ActivityService
  override val userService = UserService

  val activityAdmin = BasicHttpCredentials("test1@test.com", "password1")
  val activityNotAdmin = BasicHttpCredentials("test2@test.com", "password1")

  "Activities Endpoint" should {
    "leave GET requests to other paths unhandled" in this {
      Get("/kermit") ~> addCredentials(activityAdmin) ~> activityOperations ~>  check  {
        handled must beFalse
      }
    }
  }

  "Activities Endpoint#activities" should {
    "return a list of activities for GET requests to activities path" in this {
      Get("/activities") ~> addCredentials(activityAdmin) ~> activityOperations ~> check {
        responseAs[Seq[Activity]] === DatabaseSupportSpec.activities
      }
    }


    "return a single activity for GET requests to activities path" in this {
      Get("/activities/1") ~> addCredentials(activityAdmin) ~> activityOperations ~> check {
        responseAs[Activity] === DatabaseSupportSpec.activities.head
      }
    }

    "return the id for DELETE requests to activities path" in this {
      Delete("/activities/1") ~> addCredentials(activityAdmin) ~> activityOperations ~> check {
        val result: Seq[Activity] = Await.result(db.run{ActivityDao.getAll}, Duration.Inf)
        result mustEqual DatabaseSupportSpec.activities.filterNot(_.id == 1)
      }
    }

    "return the correct activity for POST requests to activities path" in this {
      Post("/activities",  ActivityDto(1, 2, 5, Some("Is Java Alive REALLYYYYY?"), Some("WE DONT NEED JAVA"), Some("Discuss about the future of Scala"), DatabaseSupportSpec.now, DatabaseSupportSpec.now, Some("github/gneotux"))
      ) ~> addCredentials(activityAdmin) ~> activityOperations ~> check {
        status mustEqual StatusCodes.Created
        responseAs[Activity] ===  Activity(3, 1, 2, 5, Some("Is Java Alive REALLYYYYY?"), Some("WE DONT NEED JAVA"), Some("Discuss about the future of Scala"), DatabaseSupportSpec.now, DatabaseSupportSpec.now, Some("github/gneotux"))
      }
    }

//    "return the correct activity for PUT requests to activities path" in this {
//      Put("/activities/1",  ActivityDto(1, 2, 5, Some("CONFIRMED,JAVA IS DEAD"), Some("WE DONT NEED JAVA"), Some("Discuss about the future of Scala"), DatabaseSupportSpec.now, DatabaseSupportSpec.now, Some("github/gneotux"))
//      ) ~> addCredentials(activityAdmin) ~> activityOperations ~> check {
//        status mustEqual StatusCodes.OK
//        responseAs[Activity] ===  Activity(1, 1, 2, 5, Some("CONFIRMED,JAVA IS DEAD"), Some("WE DONT NEED JAVA"), Some("Discuss about the future of Scala"), DatabaseSupportSpec.now, DatabaseSupportSpec.now, Some("github/gneotux"))
//      }
//    }.isPending

    //    "return not authorized for activity without valid permission for DELETE requests to activities path" in this {
    //      Delete("/activities/1") ~> addCredentials(activityNotAdmin) ~> activityOperations ~> check {
    //        status mustEqual StatusCodes.InternalServerError
    //      }
    //    }.isSkipped
    //
    //    "return not authorized for activity without valid permission for POST requests to activities path" in this {
    //      Post("/activities", ActivityDto("test4@test.com", Some("name4"), Some("surname4"), None, None, None, "USER", "NEWPASSWORD")) ~> addCredentials(activityNotAdmin) ~> activityOperations ~> check {
    //        status mustEqual StatusCodes.InternalServerError
    //      }
    //    }.isSkipped
  }

  "Activities Endpoint#atendees" should {
    "return a list of attendees for GET requests to activities/attendees path" in this {
      Get("/activities/1/attendees") ~> addCredentials(activityAdmin) ~> activityOperations ~> check {
        responseAs[Seq[User]] === DatabaseSupportSpec.users.filter(_.id == 2)
      }
    }

    "return the id for DELETE requests to activities/attendees path" in this {
      Delete("/activities/1/attendees/2") ~> addCredentials(activityAdmin) ~> activityOperations ~> check {
        status mustEqual StatusCodes.OK
        val result: Seq[Attendee] = Await.result(db.run{AttendeeDao.getAll}, Duration.Inf)
        result mustEqual DatabaseSupportSpec.attendees.filterNot(a => a.userId == 2 && a.activityId == 1)
      }
    }

    "return the correct attendee for POST requests to activities/atendee path" in this {
      Post("/activities/1/attendees/3") ~> addCredentials(activityAdmin) ~> activityOperations ~> check {
        status mustEqual StatusCodes.Created
        responseAs[Attendee] ===  Attendee(2, 3, 1)
      }
    }
  }

  "Activities Endpoint#speakers" should {
    "return a list of speakers for GET requests to activities/speakers path" in this {
      Get("/activities/1/speakers") ~> addCredentials(activityAdmin) ~> activityOperations ~> check {
        responseAs[Seq[User]] === DatabaseSupportSpec.users.filter(_.id == 1)
      }
    }

    "return the id for DELETE requests to activities/speakers path" in this {
      Delete("/activities/1/speakers/2") ~> addCredentials(activityAdmin) ~> activityOperations ~> check {
        status mustEqual StatusCodes.OK
        val result: Seq[Speaker] = Await.result(db.run{SpeakerDao.getAll}, Duration.Inf)
        result mustEqual DatabaseSupportSpec.speakers.filterNot(a => a.userId == 2 && a.activityId == 1)
      }
    }

    "return the correct speaker for POST requests to activities/speaker path" in this {
      Post("/activities/1/speakers/3") ~> addCredentials(activityAdmin) ~> activityOperations ~> check {
        status mustEqual StatusCodes.Created
        responseAs[Speaker] ===  Speaker(2, 3, 1)
      }
    }
  }

  "Activities Endpoint#tags" should {
    "return a list of tags for GET requests to activities/tags path" in this {
      Get("/activities/1/tags") ~> addCredentials(activityAdmin) ~> activityOperations ~> check {
        responseAs[Seq[Tag]] === DatabaseSupportSpec.tags
      }
    }

    "return the id for DELETE requests to activities/tags path" in this {
      Delete("/activities/1/tags/2") ~> addCredentials(activityAdmin) ~> activityOperations ~> check {
        status mustEqual StatusCodes.OK
        val result: Seq[ActivityTag] = Await.result(db.run{ActivityTagDao.getAll}, Duration.Inf)
        result mustEqual DatabaseSupportSpec.activityTags.filterNot(a => a.tagId == 2 && a.activityId == 1)      }
    }

    "return the correct tag for POST requests to activities/tag path" in this {
      Post("/activities/1/tags/3") ~> addCredentials(activityAdmin) ~> activityOperations ~> check {
        status mustEqual StatusCodes.Created
        responseAs[ActivityTag] ===  ActivityTag(4, 3, 1)
      }
    }
  }


}
