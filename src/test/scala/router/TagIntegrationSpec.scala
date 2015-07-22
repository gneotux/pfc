package router

import dao._
import model._
import org.specs2.mock._
import org.specs2.mutable.Specification
import router.dto.TagDto
import service.{ TagService, UserService }
import spray.http.{ BasicHttpCredentials, StatusCodes }
import spray.httpx.SprayJsonSupport._
import spray.testkit.Specs2RouteTest
import utils.DatabaseConfig._
import utils.{ DatabaseSupportSpec, SpecSupport }

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class TagIntegrationSpec extends Specification with Specs2RouteTest with TagRouter with SpecSupport with Authenticator with Mockito {

  // connects the DSL to the test ActorSystem
  implicit def actorRefFactory = system

  override val tagService = TagService
  override val userService = UserService

  val userAdmin = BasicHttpCredentials("test1@test.com", "password1")
  val userNotAdmin = BasicHttpCredentials("test2@test.com", "password1")

  "Companies Endpoint" should {
    "leave GET requests to other paths unhandled" in this {
      Get("/kermit") ~> addCredentials(userAdmin) ~> tagOperations ~>  check  {
        handled must beFalse
      }
    }
  }

  "Companies Endpoint#tags" should {
    "return a list of tags for GET requests to tags path" in this {
      Get("/tags") ~> addCredentials(userAdmin) ~> tagOperations ~> check {
        responseAs[Seq[Tag]] === DatabaseSupportSpec.tags
      }
    }


    "return a single tag for GET requests to tags path" in this {
      Get("/tags/1") ~> addCredentials(userAdmin) ~> tagOperations ~> check {
        responseAs[Tag] === DatabaseSupportSpec.tags.filter(_.id == 1).head
      }
    }

    "return the id for DELETE requests to tags path" in this {
      Delete("/tags/1") ~> addCredentials(userAdmin) ~> tagOperations ~> check {
        val result: Seq[Tag] = Await.result(db.run{TagDao.getAll}, Duration.Inf)
        result mustEqual DatabaseSupportSpec.tags.filterNot(_.id == 1)
      }
    }

    "return the correct tag for POST requests to tags path" in this {
      Post("/tags", TagDto("super advanced users", Some("blood"), Some("SUPADVANCE"))

      ) ~> addCredentials(userAdmin) ~> tagOperations ~> check {
        status mustEqual StatusCodes.Created
        responseAs[Tag] ===  Tag(4, "super advanced users", Some("blood"), Some("SUPADVANCE"))
      }
    }

    //    "return not authorized for tag without valid permission for DELETE requests to tags path" in this {
    //      Delete("/tags/1") ~> addCredentials(userNotAdmin) ~> tagOperations ~> check {
    //        status mustEqual StatusCodes.InternalServerError
    //      }
    //    }.isSkipped
    //
    //    "return not authorized for tag without valid permission for POST requests to tags path" in this {
    //      Post("/tags", TagDto("test4@test.com", Some("name4"), Some("surname4"), None, None, None, "USER", "NEWPASSWORD")) ~> addCredentials(userNotAdmin) ~> tagOperations ~> check {
    //        status mustEqual StatusCodes.InternalServerError
    //      }
    //    }.isSkipped
  }
}
