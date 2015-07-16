package model

import com.wordnik.swagger.annotations.{ ApiModel, ApiModelProperty }
import org.joda.time.DateTime
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

@ApiModel(description = "An Activity entity")
case class Activity(
  @(ApiModelProperty@field)(value = "unique identifier for the activity")
  id: Int,

  @(ApiModelProperty@field)(value = "event id")
  eventId: Int,

  @(ApiModelProperty@field)(value = "location id")
  locationId: Int,

  @(ApiModelProperty@field)(value = "activity type")
  activityTypeId: Int,

  @(ApiModelProperty@field)(value = "activity's title")
  title: Option[String] = None,

  @(ApiModelProperty@field)(value = "activity's description")
  description: Option[String] = None,

  @(ApiModelProperty@field)(value = "activity's objective in case of being a workshop")
  objective: Option[String] = None,

  @(ApiModelProperty@field)(value = "activity's start time")
  startTime: DateTime,

  @(ApiModelProperty@field)(value = "activity's end time")
  endTime: DateTime,

  @(ApiModelProperty@field)(value = "resources may be a zip file with source code, links, pdf with instructions, powerpoint with slides, a link to youtube, etc (so, potentially anything) ")
  resources: Option[String] = None
)

object Activity extends DefaultJsonProtocol {
  implicit val activityFormat = jsonFormat10(Activity.apply)
}
