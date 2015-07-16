package model

import com.wordnik.swagger.annotations.{ ApiModel, ApiModelProperty }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 16/07/15.
 */
@ApiModel(description = "An sponsor entity")
case class Sponsor(
  @(ApiModelProperty@field)(value = "unique identifier for the company-event relation")
  id: Int,

  @(ApiModelProperty@field)(value = "company id")
  companyId: Int,

  @(ApiModelProperty@field)(value = "event id")
  eventId: Int
)

object Sponsor extends DefaultJsonProtocol {
  implicit val sponsorFormat = jsonFormat3(Sponsor.apply)
}
