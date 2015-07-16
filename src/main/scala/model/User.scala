package model

import com.wordnik.swagger.annotations.{ ApiModel, ApiModelProperty }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field


@ApiModel(description = "An User entity")
case class User(
  @(ApiModelProperty @field)(value = "unique identifier for the user")
  id: Int,

  @(ApiModelProperty @field)(value = "email of the user")
  email: String,

  @(ApiModelProperty @field)(value = "user's first name")
  firstName: Option[String] = None,

  @(ApiModelProperty @field)(value = "user's last name")
  lastName: Option[String] = None,

  @(ApiModelProperty @field)(value = "user's twitter id")
  twitterId: Option[String] = None,

  @(ApiModelProperty @field)(value = "user's linkedin id")
  linkedinId: Option[String] = None,

  @(ApiModelProperty @field)(value = "user's bio")
  bio: Option[String] = None,

  @(ApiModelProperty @field)(hidden = true)
  passwordId: Option[Int] = None
)
object User extends DefaultJsonProtocol{
  implicit val userFormat = jsonFormat8(User.apply)
}
