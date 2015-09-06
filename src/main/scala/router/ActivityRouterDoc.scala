package router

import javax.ws.rs.Path

import com.wordnik.swagger.annotations._
import model._
import spray.routing._

/**
 * Created by gneotux on 17/07/15.
 */
@Api(value = "/activities", description = "Operations for activities.", consumes= "application/json",  produces = "application/json")
trait ActivityRouterDoc {

  @ApiOperation(value = "Get a activity by id", httpMethod = "GET", response = classOf[Activity])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity that needs to retrieved", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 404, message = "Activity not found")
  ))
  def readRouteActivity: Route

  @ApiOperation(value = "Get all the activities", httpMethod = "GET", responseContainer = "List", response = classOf[Activity])
  def readAllRouteActivity: Route

  @ApiOperation(value = "Delete a activity by id", httpMethod = "DELETE", response = classOf[Int])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity that needs to be deleted", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Activity not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def deleteRouteActivity: Route

  @ApiOperation(value = "Update an activity ", httpMethod = "PUT", consumes="application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity that needs to be updated", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "body", value="Activity object to be updated", required = true, dataType = "router.dto.ActivityDto", paramType = "body" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid activity"),
    new ApiResponse(code = 200, message = "Entity Updated")
  ))
  def updateRouteActivity: Route

  @ApiOperation(value = "Add a new activity to the system", httpMethod = "POST", consumes="application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value="Activity object to be added", required = true, dataType = "router.dto.ActivityDto", paramType = "body" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid activity"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def postRouteActivity: Route

  @ApiOperation(value = "Get all the attendees in a activity by activityId", httpMethod = "GET", responseContainer = "List", response = classOf[User])
  @Path("/{activityId}/attendees")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 404, message = "Activity not found")
  ))
  def readAllAttendeesInActivity: Route

  @ApiOperation(value = "Add a new attendee for the Activity", httpMethod = "POST", response = classOf[Attendee])
  @Path("/{activityId}/attendees/{userId}")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "userId", value="ID of the user", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid activity"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def postRouteActivityAttendee: Route

  @ApiOperation(value = "Remove an attendee for the Activity", httpMethod = "DELETE", response = classOf[Int])
  @Path("/{activityId}/attendees/{userId}")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "userId", value="ID of the user", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Entity not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def deleteRouteActivityAttendee: Route

  @ApiOperation(value = "Get all the speakers in a activity by activityId", httpMethod = "GET", responseContainer = "List", response = classOf[User])
  @Path("/{activityId}/speakers")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 404, message = "Activity not found")
  ))
  def readAllSpeakersInActivity: Route

  @ApiOperation(value = "Add new speaker for the activity", httpMethod = "POST", response = classOf[Speaker])
  @Path("/{activityId}/speakers/{userId}")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "userId", value="ID of the user", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid activity"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def postRouteActivitySpeaker: Route

  @ApiOperation(value = "Remove a speaker for the activity", httpMethod = "DELETE", response = classOf[Int])
  @Path("/{activityId}/speakers/{userId}")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "userId", value="ID of the user", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Entity not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def deleteRouteActivitySpeaker: Route

  @ApiOperation(value = "Get all the tags in a activity by activityId", httpMethod = "GET", responseContainer = "List", response = classOf[Tag])
  @Path("/{activityId}/tags")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 404, message = "Activity not found")
  ))
  def readAllTagsInActivity: Route

  @ApiOperation(value = "Add new tag to the activity", httpMethod = "POST", response = classOf[ActivityTag])
  @Path("/{activityId}/tags/{tagId}")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "tagId", value="ID of the tag", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid activity"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def postRouteActivityTag: Route

  @ApiOperation(value = "Remove a tag of the Activity", httpMethod = "DELETE", response = classOf[Int])
  @Path("/{activityId}/tags/{tagId}")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "tagId", value="ID of the tag", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Entity not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def deleteRouteActivityTag: Route

}
