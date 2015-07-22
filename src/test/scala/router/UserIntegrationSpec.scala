package router

import dao.UserDao
import model.User
import org.specs2.mock._
import org.specs2.mutable.Specification
import router.dto.UserDto
import service.UserService
import spray.http.{ StatusCodes, BasicHttpCredentials }
import spray.httpx.SprayJsonSupport._
import spray.testkit.Specs2RouteTest
import utils.{ DatabaseSupportSpec, SpecSupport }
import utils.DatabaseConfig.db

import scala.concurrent.Await
import scala.concurrent.duration.Duration



class UserIntegrationSpec extends Specification with Specs2RouteTest with UserRouter with SpecSupport with Authenticator with Mockito {

  // connects the DSL to the test ActorSystem
  implicit def actorRefFactory = system

  override val userService = UserService

  val userAdmin = BasicHttpCredentials("test1@test.com", "password1")
  val userNotAdmin = BasicHttpCredentials("test2@test.com", "password1")

  "Users Endpoint" should {
    "leave GET requests to other paths unhandled" in this {
      Get("/kermit") ~> addCredentials(userAdmin) ~> userOperations ~>  check  {
        handled must beFalse
      }
    }
  }

  "Users Endpoint#users" should {
    "return a list of users for GET requests to users path" in this {
      Get("/users") ~> addCredentials(userAdmin) ~> userOperations ~> check {
        responseAs[Seq[User]] === DatabaseSupportSpec.users
      }
    }


    "return a single user for GET requests to users path" in this {
      Get("/users/1") ~> addCredentials(userAdmin) ~> userOperations ~> check {
        responseAs[User] === DatabaseSupportSpec.users.head
      }
    }

    "return the id for DELETE requests to users path" in this {
      Delete("/users/1") ~> addCredentials(userAdmin) ~> userOperations ~> check {
        status mustEqual StatusCodes.OK
        val result: Seq[User] = Await.result(db.run{UserDao.getAll}, Duration.Inf)
        result mustEqual DatabaseSupportSpec.users.filterNot(_.id == 1)
      }
    }

    "return the correct user for POST requests to users path" in this {
      Post("/users", UserDto("test4@test.com", Some("name4"), Some("surname4"), None, None, None, "USER", "NEWPASSWORD")) ~> addCredentials(userAdmin) ~> userOperations ~> check {
        status mustEqual StatusCodes.Created
        responseAs[User] === User(4, "test4@test.com", Some("name4"), Some("surname4"), None, None, None, "USER", Some(4))
      }
    }

//    "return not authorized for user without valid permission for DELETE requests to users path" in this {
//      Delete("/users/1") ~> addCredentials(userNotAdmin) ~> userOperations ~> check {
//        status mustEqual StatusCodes.InternalServerError
//      }
//    }.isSkipped
//
//    "return not authorized for user without valid permission for POST requests to users path" in this {
//      Post("/users", UserDto("test4@test.com", Some("name4"), Some("surname4"), None, None, None, "USER", "NEWPASSWORD")) ~> addCredentials(userNotAdmin) ~> userOperations ~> check {
//        status mustEqual StatusCodes.InternalServerError
//      }
//    }.isSkipped
  }


}
