package model

import com.wordnik.swagger.annotations.{ ApiModelProperty, ApiModel }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 16/07/15.
 */
@ApiModel(description = "A Location entity")
case class Location(
  @(ApiModelProperty@field)(value = "unique identifier for the location")
  id: Int,

  @(ApiModelProperty@field)(value = "location name")
  name: String,

  @(ApiModelProperty@field)(value = "location's code")
  code: Option[String] = None,

  @(ApiModelProperty @field)(value = "location's latitude")
  latitude: Double,

  @(ApiModelProperty @field)(value = "location's longitude")
  longitude: Double,

  @(ApiModelProperty @field)(value = "location's people capacity")
  capacity: Int,

  @(ApiModelProperty@field)(value = "location's description")
  description: Option[String] = None,

  @(ApiModelProperty@field)(value = "location's photo url")
  photoUrl: Option[String] = None
)

object Location extends DefaultJsonProtocol {
  implicit val locationFormat = jsonFormat8(Location.apply)
}
