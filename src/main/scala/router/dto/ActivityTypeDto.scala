package router
package dto

import com.wordnik.swagger.annotations.{ ApiModel, ApiModelProperty }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 16/07/15.
 */
@ApiModel(description = "An activity type entity")
case class ActivityTypeDto(
  @(ApiModelProperty@field)(required = true, value = "activity type description")
  description: String
)

object ActivityTypeDto extends DefaultJsonProtocol {
  implicit val activityTypeDtoFormat = jsonFormat1(ActivityTypeDto.apply)
}
