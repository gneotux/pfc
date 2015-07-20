package model

import com.wordnik.swagger.annotations.{ ApiModel, ApiModelProperty }
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
 * Created by gneotux on 20/07/15.
 */
@ApiModel(description = "An Permission entity")
case class Permission(
  @(ApiModelProperty@field)(value = "unique identifier for the permission")
  id: Int,

  @(ApiModelProperty@field)(value = "permission name")
  name: String,

  @(ApiModelProperty@field)(value = "permission's description")
  description: Option[String] = None
)

object Permission extends DefaultJsonProtocol {
  implicit val permissionFormat = jsonFormat3(Permission.apply)
}

