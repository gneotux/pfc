package model

import com.wordnik.swagger.annotations.{ ApiModelProperty, ApiModel }
import org.joda.time.DateTime
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 16/07/15.
 */
@ApiModel(description = "An event day entity")
case class EventDay(
  @(ApiModelProperty@field)(value = "unique identifier for the event day")
  id: Int,

  @(ApiModelProperty@field)(value = "event id")
  eventId: Int,

  @(ApiModelProperty@field)(value = "event day start time")
  startTime: DateTime,

  @(ApiModelProperty@field)(value = "event day end time")
  endTime: DateTime
)

object EventDay extends DefaultJsonProtocol {
  implicit val eventDayFormat = jsonFormat4(EventDay.apply)
}
