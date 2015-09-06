package router

import com.wordnik.swagger.annotations._
import spray.routing._
import model.Company

// Trying to not pollute the code with annotations
@Api(value = "/companies", description = "Operations for companies.", consumes= "application/json",  produces = "application/json")
trait CompanyRouterDoc {

  @ApiOperation(value = "Get a company by id", httpMethod = "GET", response = classOf[Company])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "companyId", value="ID of the company that needs to retrieved", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 404, message = "Company not found")
  ))
  def readRouteCompany: Route

  @ApiOperation(value = "Get all the companies", httpMethod = "GET", responseContainer = "List", response = classOf[Company])
  def readAllRouteCompany: Route

  @ApiOperation(value = "Delete a company by id", httpMethod = "DELETE", response = classOf[Int])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "companyId", value="ID of the company that needs to be deleted", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Company not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def deleteRouteCompany: Route


  @ApiOperation(value = "Add a new company to the system", httpMethod = "POST", consumes="application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value="Company object to be added", required = true, dataType = "router.dto.CompanyDto", paramType = "body" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid company"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def postRouteCompany: Route

  @ApiOperation(value = "Update the company", httpMethod = "PUT", consumes="application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "companyId", value="ID of the company that needs to be updated", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "body", value="Company object to be updated", required = true, dataType = "router.dto.CompanyDto", paramType = "body" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid company"),
    new ApiResponse(code = 200, message = "Entity Updated")
  ))
  def updateRouteCompany: Route

}
