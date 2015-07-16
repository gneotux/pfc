package model

import com.wordnik.swagger.annotations.{ ApiModel, ApiModelProperty }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 16/07/15.
 */
@ApiModel(description = "An activity type entity")
case class ActivityType(
  @(ApiModelProperty@field)(value = "unique identifier for the activity type")
  id: Int,

  @(ApiModelProperty@field)(value = "activity type description")
  description: String
)

object ActivityType extends DefaultJsonProtocol {
  implicit val activityTypeFormat = jsonFormat2(ActivityType.apply)
}
