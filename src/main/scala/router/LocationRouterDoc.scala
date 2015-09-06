package router

import com.wordnik.swagger.annotations._
import model.Location
import spray.routing._

/**
 * Created by gneotux on 18/07/15.
 */
@Api(value = "/locations", description = "Operations for locations.", consumes= "application/json",  produces = "application/json")
trait LocationRouterDoc {

  @ApiOperation(value = "Get a location by id", httpMethod = "GET", response = classOf[Location])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "locationId", value="ID of the location that needs to retrieved", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 404, message = "Location not found")
  ))
  def readRouteLocation: Route

  @ApiOperation(value = "Get all the locations", httpMethod = "GET", responseContainer = "List", response = classOf[Location])
  def readAllRouteLocation: Route

  @ApiOperation(value = "Delete a location by id", httpMethod = "DELETE", response = classOf[Int])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "locationId", value="ID of the location that needs to be deleted", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Location not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def deleteRouteLocation: Route


  @ApiOperation(value = "Add a new location to the system", httpMethod = "POST", consumes="application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value="Location object to be added", required = true, dataType = "router.dto.LocationDto", paramType = "body" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid location"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def postRouteLocation: Route

  @ApiOperation(value = "Update a location", httpMethod = "PUT", consumes="application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "locationId", value="ID of the location that needs to be updated", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "body", value="Location object to be updated", required = true, dataType = "router.dto.LocationDto", paramType = "body" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid location"),
    new ApiResponse(code = 200, message = "Entity Updated")
  ))
  def updateRouteLocation: Route

}