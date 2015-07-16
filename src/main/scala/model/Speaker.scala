package model

import com.wordnik.swagger.annotations.{ ApiModelProperty, ApiModel }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 16/07/15.
 */
@ApiModel(description = "An speaker entity")
case class Speaker(
  @(ApiModelProperty@field)(value = "unique identifier for the speaker-activity relation")
  id: Int,

  @(ApiModelProperty@field)(value = "user id")
  userId: Int,

  @(ApiModelProperty@field)(value = "activity id")
  activityId: Int
)

object Speaker extends DefaultJsonProtocol {
  implicit val speakerFormat = jsonFormat3(Speaker.apply)
}
