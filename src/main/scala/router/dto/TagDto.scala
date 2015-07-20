package router
package dto

import com.wordnik.swagger.annotations.{ ApiModelProperty, ApiModel }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field


@ApiModel(description = "An TagDto entity")
case class TagDto(
  @(ApiModelProperty@field)(required = true, value = "tag name")
  name: String,

  @(ApiModelProperty@field)(value = "tag's color")
  color: Option[String] = None,

  @(ApiModelProperty@field)(value = "tag's shortName")
  shortName: Option[String] = None
)

object TagDto extends DefaultJsonProtocol {
  implicit val tagFormat = jsonFormat3(TagDto.apply)
}
