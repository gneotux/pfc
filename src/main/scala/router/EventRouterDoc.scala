package router

import javax.ws.rs.Path

import com.wordnik.swagger.annotations._
import model.{ Company, Sponsor, Event }
import spray.routing._

/**
 * Created by gneotux on 17/07/15.
 */
@Api(value = "/events", description = "Operations for events.", consumes= "application/json",  produces = "application/json")
trait EventRouterDoc {

  @ApiOperation(value = "Get a event by id", httpMethod = "GET", response = classOf[Event])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "eventId", value="ID of the event that needs to retrieved", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 404, message = "Event not found")
  ))
  def readRouteEvent: Route

  @ApiOperation(value = "Get all the events", httpMethod = "GET", response = classOf[List[Event]])
  def readAllRouteEvent: Route

  @ApiOperation(value = "Delete a event by id", httpMethod = "DELETE", response = classOf[Int])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "eventId", value="ID of the event that needs to be deleted", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Event not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def deleteRouteEvent: Route


  @ApiOperation(value = "Add a new event to the system", httpMethod = "POST", consumes="application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value="Event object to be added", required = true, dataType = "router.dto.EventDto", paramType = "body" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid event"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def postRouteEvent: Route

  @ApiOperation(value = "Get all the sponsors in a event by eventId", httpMethod = "GET", response = classOf[Company])
  @Path("/{eventId}/sponsors")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "eventId", value="ID of the event", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 404, message = "Event not found")
  ))
  def readAllSponsorsInEvent: Route

  @ApiOperation(value = "Add new sponsor for the event", httpMethod = "POST", response = classOf[Sponsor])
  @Path("/{eventId}/sponsors/{companyId}")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "eventId", value="ID of the event", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "companyId", value="ID of the company", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid entity"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def postRouteEventSponsor: Route

  @ApiOperation(value = "Remove an sponsor for the event", httpMethod = "DELETE", response = classOf[Int])
  @Path("/{eventId}/sponsors/{userId}")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "eventId", value="ID of the event", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "companyId", value="ID of the company", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Entity not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def deleteRouteEventSponsor: Route

}