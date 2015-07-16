package model

import com.wordnik.swagger.annotations.{ ApiModelProperty, ApiModel }
import org.joda.time.DateTime
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 16/07/15.
 */
@ApiModel(description = "An atendee entity")
case class Atendee(
  @(ApiModelProperty@field)(value = "unique identifier for the atendee-activity relation")
  id: Int,

  @(ApiModelProperty@field)(value = "user id")
  userId: Int,

  @(ApiModelProperty@field)(value = "activity id")
  activityId: Int
)

object Atendee extends DefaultJsonProtocol {
  implicit val atendeeFormat = jsonFormat3(Atendee.apply)
}
