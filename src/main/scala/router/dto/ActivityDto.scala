package router
package dto

import com.wordnik.swagger.annotations.{ ApiModel, ApiModelProperty }
import model.Activity._
import org.joda.time.DateTime
import spray.json.DefaultJsonProtocol
import utils.ApiFormats._

import scala.annotation.meta.field

/**
 * Created by gneotux on 17/07/15.
 */
@ApiModel(description = "An Activity creation entity")
case class ActivityDto(

  @(ApiModelProperty@field)(required = true, value = "event id")
  eventId: Int,

  @(ApiModelProperty@field)(required = true, value = "location id")
  locationId: Int,

  @(ApiModelProperty@field)(required = true, value = "activity type")
  activityTypeId: Int,

  @(ApiModelProperty@field)(value = "activity's title")
  title: Option[String] = None,

  @(ApiModelProperty@field)(value = "activity's description")
  description: Option[String] = None,

  @(ApiModelProperty@field)(value = "activity's objective in case of being a workshop")
  objective: Option[String] = None,

  @(ApiModelProperty@field)(required = true, value = "activity's start time")
  startTime: DateTime,

  @(ApiModelProperty@field)(required = true, value = "activity's end time")
  endTime: DateTime,

  @(ApiModelProperty@field)(value = "resources may be a zip file with source code, links, pdf with instructions, powerpoint with slides, a link to youtube, etc (so, potentially anything) ")
  resources: Option[String] = None
)

object ActivityDto extends DefaultJsonProtocol {
  implicit val activityDtoFormat = jsonFormat9(ActivityDto.apply)
}
