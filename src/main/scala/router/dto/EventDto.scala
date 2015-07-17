package router
package dto

import com.wordnik.swagger.annotations.{ ApiModelProperty, ApiModel }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 17/07/15.
 */
@ApiModel(description = "An EventDto entity")
case class EventDto(
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

object EventDto extends DefaultJsonProtocol {
  implicit val eventFormat = jsonFormat5(EventDto.apply)
}
