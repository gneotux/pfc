package router.dto

import com.wordnik.swagger.annotations.{ ApiModelProperty, ApiModel }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 16/07/15.
 */
@ApiModel(description = "An Company creation entity")
case class CompanyDto(
  @(ApiModelProperty @field)(required = true, value = "email for the contact of the company")
  email: String,

  @(ApiModelProperty @field)(value = "phone number for the contact of the company")
  phone: Option[String] = None,

  @(ApiModelProperty@field)(value = "company's description")
  description: Option[String] = None,

  @(ApiModelProperty@field)(value = "company's website")
  website: Option[String] = None,

  @(ApiModelProperty@field)(value = "company's logo url")
  logoUrl: Option[String] = None
)
object CompanyDto extends DefaultJsonProtocol{
  implicit val companyFormat = jsonFormat5(CompanyDto.apply)
}