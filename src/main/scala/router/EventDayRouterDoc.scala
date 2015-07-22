package router

import com.wordnik.swagger.annotations._
import model.EventDay
import spray.routing._

/**
 * Created by gneotux on 20/07/15.
 */
@Api(value = "/eventdays", description = "Operations for eventdays.", consumes= "application/json",  produces = "application/json")
trait EventDayRouterDoc {

  @ApiOperation(value = "Get a eventday by id", httpMethod = "GET", response = classOf[EventDay])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "eventdayId", value="ID of the eventday that needs to retrieved", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 404, message = "EventDay not found")
  ))
  def readRouteEventDay: Route

  @ApiOperation(value = "Get all the eventdays", httpMethod = "GET", responseContainer = "List", response = classOf[EventDay])
  def readAllRouteEventDay: Route

  @ApiOperation(value = "Delete a eventday by id", httpMethod = "DELETE", response = classOf[Int])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "eventdayId", value="ID of the eventday that needs to be deleted", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Event day not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def deleteRouteEventDay: Route


  @ApiOperation(value = "Add a new eventday to the system", httpMethod = "POST", consumes="application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value="Event day object to be added", required = true, dataType = "router.dto.EventDayDto", paramType = "body" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid event day"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def postRouteEventDay: Route

}