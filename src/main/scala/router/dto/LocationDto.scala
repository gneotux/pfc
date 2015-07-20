package router
package dto

import com.wordnik.swagger.annotations.{ ApiModelProperty, ApiModel }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 18/07/15.
 */
@ApiModel(description = "A Location creation entity")
case class LocationDto(
  @(ApiModelProperty@field)(required = true, value = "location name")
  name: String,

  @(ApiModelProperty@field)(value = "location's code")
  code: Option[String] = None,

  @(ApiModelProperty @field)(required = true, value = "location's latitude")
  latitude: Double,

  @(ApiModelProperty @field)(required = true, value = "location's longitude")
  longitude: Double,

  @(ApiModelProperty @field)(required = true, value = "location's people capacity")
  capacity: Int,

  @(ApiModelProperty@field)(value = "location's description")
  description: Option[String] = None,

  @(ApiModelProperty@field)(value = "location's photo url")
  photoUrl: Option[String] = None
)

object LocationDto extends DefaultJsonProtocol {
  implicit val locationDtoFormat = jsonFormat7(LocationDto.apply)
}
