package model

import com.wordnik.swagger.annotations.{ ApiModelProperty, ApiModel }
import org.joda.time.DateTime
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 16/07/15.
 */
@ApiModel(description = "An atendee entity")
case class Attendee(
  @(ApiModelProperty@field)(value = "unique identifier for the attendee-activity relation")
  id: Int,

  @(ApiModelProperty@field)(value = "user id")
  userId: Int,

  @(ApiModelProperty@field)(value = "activity id")
  activityId: Int
)

object Attendee extends DefaultJsonProtocol {
  implicit val attendeeFormat = jsonFormat3(Attendee.apply)
}
