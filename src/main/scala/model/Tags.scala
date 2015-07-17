package model

import com.wordnik.swagger.annotations.{ ApiModelProperty, ApiModel }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 17/07/15.
 */
@ApiModel(description = "An Tag entity")
case class Tag(
  @(ApiModelProperty@field)(value = "unique identifier for the tag")
  id: Int,

  @(ApiModelProperty@field)(value = "tag name")
  name: String,

  @(ApiModelProperty@field)(value = "tag's color")
  color: Option[String] = None,

  @(ApiModelProperty@field)(value = "tag's shortName")
  shortName: Option[String] = None
)

object Tag extends DefaultJsonProtocol {
  implicit val tagFormat = jsonFormat4(Tag.apply)
}
