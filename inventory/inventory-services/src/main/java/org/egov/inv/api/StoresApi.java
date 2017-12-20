/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.0-SNAPSHOT).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.egov.inv.api;


import io.swagger.annotations.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.StoreRequest;
import org.egov.inv.model.StoreResponse;
import org.egov.inv.model.TransactionUsedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

@Api(value = "stores", description = "the stores API")
public interface StoresApi {

    @ApiOperation(value = "Create  new  stores", nickname = "storesCreatePost", notes = "Create  new stores", response = StoreResponse.class, tags = {"Inventory", "Store",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Store created Successfully", response = StoreResponse.class),
            @ApiResponse(code = 400, message = "Invalid Input", response = ErrorRes.class)})
    @RequestMapping(value = "/stores/_create",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<StoreResponse> storesCreatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId, @ApiParam(value = "Create  new") @Valid @RequestBody StoreRequest storeRequest, @RequestHeader(value = "Accept", required = false) String accept) throws Exception;


    @ApiOperation(value = "Get the list of stores", nickname = "storesSearchPost", notes = "stores", response = StoreResponse.class, tags = {"Inventory", "Store",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Store retrieved Successfully", response = StoreResponse.class),
            @ApiResponse(code = 400, message = "Invalid Input", response = ErrorRes.class)})
    @RequestMapping(value = "/stores/_search",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<StoreResponse> storesSearchPost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId, @ApiParam(value = "Parameter to carry Request metadata in the request body") @Valid @RequestBody org.egov.common.contract.request.RequestInfo RequestInfo, @Size(max = 50) @ApiParam(value = "comma seperated list of Ids") @Valid @RequestParam(value = "ids", required = false) List<String> ids, @ApiParam(value = "list of code of the Store ") @Valid @RequestParam(value = "codes", required = false) List<String> codes, @ApiParam(value = "name of the Store ") @Valid @RequestParam(value = "name", required = false) String name, @ApiParam(value = "search purposeof store ") @Valid @RequestParam(value = "searchPurpose", required = false) String searchPurpose, @ApiParam(value = "description of the Store ") @Valid @RequestParam(value = "description", required = false) String description, @ApiParam(value = "department of the Store ") @Valid @RequestParam(value = "department", required = false) String department, @ApiParam(value = "billing address of the Store ") @Valid @RequestParam(value = "billingAddress", required = false) String billingAddress, @ApiParam(value = "delivery address of the Store ") @Valid @RequestParam(value = "deliveryAddress", required = false) String deliveryAddress, @ApiParam(value = "contact no1 of the Store ") @Valid @RequestParam(value = "contactNo1", required = false) String contactNo1, @ApiParam(value = "contact no2 of the Store ") @Valid @RequestParam(value = "contactNo2", required = false) String contactNo2, @ApiParam(value = "contact no2 of the Store ") @Valid @RequestParam(value = "officelocation", required = false) String officelocation, @ApiParam(value = "email of the Store ") @Valid @RequestParam(value = "email", required = false) String email, @ApiParam(value = "store in charge of the Store ") @Valid @RequestParam(value = "storeInCharge", required = false) String storeInCharge, @ApiParam(value = "is central store of the Store ") @Valid @RequestParam(value = "isCentralStore", required = false) Boolean isCentralStore, @ApiParam(value = "Whether Store is Active or not. If the value is TRUE, then Store is active,If the value is FALSE then Store is inactive,Default value is TRUE ") @Valid @RequestParam(value = "active", required = false) Boolean active, @ApiParam(value = "pageSize") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize, @ApiParam(value = "offset") @Valid @RequestParam(value = "offset", required = false) Integer offset, @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords.   example name asc,code desc or name,code or name,code desc  ") @Valid @RequestParam(value = "sortBy", required = false) String sortBy, @RequestHeader(value = "Accept", required = false) String accept) throws Exception;


    @ApiOperation(value = "Update any of the stores", nickname = "storesUpdatePost", notes = "Update any of the stores", response = StoreResponse.class, tags = {"Inventory", "Store",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Store updated Successfully", response = StoreResponse.class),
            @ApiResponse(code = 400, message = "Invalid Input", response = ErrorRes.class)})
    @RequestMapping(value = "/stores/_update",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<StoreResponse> storesUpdatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId, @ApiParam(value = "common Request info") @Valid @RequestBody StoreRequest storeRequest, @RequestHeader(value = "Accept", required = false) String accept) throws Exception;

    @ApiOperation(value = "Check whether store is used in transaction", notes = "Check whether store is used in transaction", response = TransactionUsedResponse.class, tags = {"Store",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Transaction used response received.", response = TransactionUsedResponse.class),
            @ApiResponse(code = 400, message = "Invalid Input", response = ErrorRes.class)})

    @RequestMapping(value = "/stores/_transactionused",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<TransactionUsedResponse> storesTransactionusedPost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId, @ApiParam(value = "Parameter to carry Request metadata in the request body") @Valid @RequestBody RequestInfo requestInfo, @ApiParam(value = "code of the Store ") @RequestParam(value = "code", required = true) String code);
}
