package model

import com.wordnik.swagger.annotations.{ ApiModel, ApiModelProperty }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 16/07/15.
 */
@ApiModel(description = "An Event entity")
case class Event(
  @(ApiModelProperty@field)(value = "unique identifier for the event")
  id: Int,

  @(ApiModelProperty@field)(value = "event name")
  name: String,

  @(ApiModelProperty@field)(value = "event's description")
  description: Option[String] = None,

  @(ApiModelProperty@field)(value = "event's website")
  website: Option[String] = None,

  @(ApiModelProperty @field)(value = "event's twitter hashtag")
  twitterHashtag: Option[String] = None,

  @(ApiModelProperty@field)(value = "event's logo url")
  logoUrl: Option[String] = None
)

object Event extends DefaultJsonProtocol {
  implicit val eventFormat = jsonFormat6(Event.apply)
}