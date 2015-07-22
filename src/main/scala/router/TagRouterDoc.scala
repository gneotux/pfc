package router

import com.wordnik.swagger.annotations._
import model.Tag
import spray.routing._


@Api(value = "/tags", description = "Operations for tags.", consumes= "application/json",  produces = "application/json")
trait TagRouterDoc {

  @ApiOperation(value = "Get a tag by id", httpMethod = "GET", response = classOf[Tag])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "tagId", value="ID of the tag that needs to retrieved", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 404, message = "Tag not found")
  ))
  def readRouteTag: Route

  @ApiOperation(value = "Get all the tags", httpMethod = "GET", responseContainer = "List", response = classOf[Tag])
  def readAllRouteTag: Route

  @ApiOperation(value = "Delete a tag by id", httpMethod = "DELETE", response = classOf[Int])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "tagId", value="ID of the tag that needs to be deleted", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Tag not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def deleteRouteTag: Route


  @ApiOperation(value = "Add a new tag to the system", httpMethod = "POST", consumes="application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value="Tag object to be added", required = true, dataType = "router.dto.TagDto", paramType = "body" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid tag"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def postRouteTag: Route

}