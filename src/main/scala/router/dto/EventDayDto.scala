package router.dto

import com.wordnik.swagger.annotations.{ ApiModel, ApiModelProperty }
import org.joda.time.DateTime
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 20/07/15.
 */
@ApiModel(description = "An event day entity")
case class EventDayDto(
  @(ApiModelProperty@field)(value = "event id")
  eventId: Int,

  @(ApiModelProperty@field)(value = "event day start time")
  startTime: DateTime,

  @(ApiModelProperty@field)(value = "event day end time")
  endTime: DateTime
)

object EventDayDto extends DefaultJsonProtocol {
  implicit val eventDayDtoFormat = jsonFormat3(EventDayDto.apply)
}