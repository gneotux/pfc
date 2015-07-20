package router
package dto

import com.wordnik.swagger.annotations.{ ApiModel, ApiModelProperty }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field


@ApiModel(description = "A User creation entity")
case class UserDto(
  @(ApiModelProperty @field)(required = true, value = "email of the user")
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

  @(ApiModelProperty @field)(required = true, value = "user permission")
  permission: String,

  @(ApiModelProperty @field)(required = true, value = "password of the user")
  password: String )

object UserDto extends DefaultJsonProtocol{
  implicit val userDtoFormat = jsonFormat8(UserDto.apply)
}
