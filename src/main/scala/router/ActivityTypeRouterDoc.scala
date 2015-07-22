package router

import javax.ws.rs.Path

import com.wordnik.swagger.annotations._
import model.{ Sponsor, ActivityType }
import spray.routing._

/**
 * Created by gneotux on 21/07/15.
 */
@Api(value = "/activityTypes", description = "Operations for activityTypes.", consumes= "application/json",  produces = "application/json")
trait ActivityTypeRouterDoc {

  @ApiOperation(value = "Get a activityType by id", httpMethod = "GET", response = classOf[ActivityType])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityTypeId", value="ID of the activityType that needs to retrieved", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 404, message = "ActivityType not found")
  ))
  def readRouteActivityType: Route

  @ApiOperation(value = "Get all the activityTypes", httpMethod = "GET", responseContainer = "List", response = classOf[ActivityType])
  def readAllRouteActivityType: Route

  @ApiOperation(value = "Delete a activityType by id", httpMethod = "DELETE", response = classOf[Int])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityTypeId", value="ID of the activityType that needs to be deleted", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "ActivityType not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def deleteRouteActivityType: Route

  @ApiOperation(value = "Add a new activityType to the system", httpMethod = "POST", consumes="application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value="ActivityType object to be added", required = true, dataType = "router.dto.ActivityTypeDto", paramType = "body" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid activityType"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def postRouteActivityType: Route

}