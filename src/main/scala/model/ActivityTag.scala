package model

import com.wordnik.swagger.annotations.{ ApiModelProperty, ApiModel }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 17/07/15.
 */
@ApiModel(description = "An activity-tag relation entity")
case class ActivityTag(
  @(ApiModelProperty@field)(value = "unique identifier for the activity-tag relation")
  id: Int,

  @(ApiModelProperty@field)(value = "tag id")
  tagId: Int,

  @(ApiModelProperty@field)(value = "activity id")
  activityId: Int
)

object ActivityTag extends DefaultJsonProtocol {
  implicit val activityTagFormat = jsonFormat3(ActivityTag.apply)
}
