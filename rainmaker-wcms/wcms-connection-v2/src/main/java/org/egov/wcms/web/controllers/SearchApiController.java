package org.egov.wcms.web.controllers;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.wcms.web.models.RequestInfo;
import org.egov.wcms.web.models.WaterConnectionRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-05-03T01:09:48.367+05:30")

@Controller
    @RequestMapping("/connection/v2")
    public class SearchApiController{

        private final ObjectMapper objectMapper;

        private final HttpServletRequest request;

        @Autowired
        public SearchApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        }

                @RequestMapping(value="/_search", method = RequestMethod.POST)
                public ResponseEntity<WaterConnectionRes> searchPost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,@ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,@ApiParam(value = "legacy consumer number of citizen") @Valid @RequestParam(value = "legacyConsumerNumber", required = false) Long legacyConsumerNumber,@Size(min=0,max=64) @ApiParam(value = "acknowledgment number of the connection") @Valid @RequestParam(value = "acknowledgmentNumber", required = false) String acknowledgmentNumber,@Size(min=0,max=64) @ApiParam(value = "HSC consumer number for the water connection") @Valid @RequestParam(value = "consumerNumber", required = false) String consumerNumber,@ApiParam(value = "name of citizen") @Valid @RequestParam(value = "name", required = false) String name,@ApiParam(value = "mobile number of citizen") @Valid @RequestParam(value = "mobileNumber", required = false) String mobileNumber) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<WaterConnectionRes>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"actionHistory\" : [ {    \"actions\" : [ {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    }, {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    } ]  }, {    \"actions\" : [ {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    }, {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    } ]  } ],  \"connections\" : [ {    \"owner\" : [ {      \"pwdExpiryDate\" : \"2000-01-23\",      \"correspondenceCity\" : \"correspondenceCity\",      \"gender\" : \"gender\",      \"signature\" : \"signature\",      \"mobileNumber\" : \"mobileNumber\",      \"roles\" : [ {        \"code\" : \"code\",        \"createdDate\" : \"2000-01-23\",        \"createdBy\" : 1,        \"lastModifiedDate\" : \"2000-01-23\",        \"lastModifiedBy\" : 6,        \"name\" : \"name\",        \"tenantId\" : \"tenantId\",        \"description\" : \"description\",        \"id\" : 1      }, {        \"code\" : \"code\",        \"createdDate\" : \"2000-01-23\",        \"createdBy\" : 1,        \"lastModifiedDate\" : \"2000-01-23\",        \"lastModifiedBy\" : 6,        \"name\" : \"name\",        \"tenantId\" : \"tenantId\",        \"description\" : \"description\",        \"id\" : 1      } ],      \"correspondencePincode\" : \"correspondencePincode\",      \"emailId\" : \"emailId\",      \"locale\" : \"locale\",      \"type\" : \"type\",      \"correspondenceAddress\" : \"correspondenceAddress\",      \"bloodGroup\" : \"bloodGroup\",      \"password\" : \"password\",      \"id\" : 1,      \"permanentAddress\" : \"permanentAddress\",      \"pan\" : \"pan\",      \"accountLocked\" : true,      \"altContactNumber\" : \"altContactNumber\",      \"identificationMark\" : \"identificationMark\",      \"lastModifiedDate\" : \"2000-01-23\",      \"lastModifiedBy\" : 1,      \"fatherOrHusbandName\" : \"fatherOrHusbandName\",      \"active\" : true,      \"photo\" : \"photo\",      \"userName\" : \"userName\",      \"aadhaarNumber\" : \"aadhaarNumber\",      \"createdDate\" : \"2000-01-23\",      \"createdBy\" : 7,      \"dob\" : \"2000-01-23\",      \"otpReference\" : \"otpReference\",      \"name\" : \"name\",      \"tenantId\" : \"tenantId\",      \"salutation\" : \"salutation\",      \"permanentCity\" : \"permanentCity\",      \"permanentPincode\" : \"permanentPincode\"    }, {      \"pwdExpiryDate\" : \"2000-01-23\",      \"correspondenceCity\" : \"correspondenceCity\",      \"gender\" : \"gender\",      \"signature\" : \"signature\",      \"mobileNumber\" : \"mobileNumber\",      \"roles\" : [ {        \"code\" : \"code\",        \"createdDate\" : \"2000-01-23\",        \"createdBy\" : 1,        \"lastModifiedDate\" : \"2000-01-23\",        \"lastModifiedBy\" : 6,        \"name\" : \"name\",        \"tenantId\" : \"tenantId\",        \"description\" : \"description\",        \"id\" : 1      }, {        \"code\" : \"code\",        \"createdDate\" : \"2000-01-23\",        \"createdBy\" : 1,        \"lastModifiedDate\" : \"2000-01-23\",        \"lastModifiedBy\" : 6,        \"name\" : \"name\",        \"tenantId\" : \"tenantId\",        \"description\" : \"description\",        \"id\" : 1      } ],      \"correspondencePincode\" : \"correspondencePincode\",      \"emailId\" : \"emailId\",      \"locale\" : \"locale\",      \"type\" : \"type\",      \"correspondenceAddress\" : \"correspondenceAddress\",      \"bloodGroup\" : \"bloodGroup\",      \"password\" : \"password\",      \"id\" : 1,      \"permanentAddress\" : \"permanentAddress\",      \"pan\" : \"pan\",      \"accountLocked\" : true,      \"altContactNumber\" : \"altContactNumber\",      \"identificationMark\" : \"identificationMark\",      \"lastModifiedDate\" : \"2000-01-23\",      \"lastModifiedBy\" : 1,      \"fatherOrHusbandName\" : \"fatherOrHusbandName\",      \"active\" : true,      \"photo\" : \"photo\",      \"userName\" : \"userName\",      \"aadhaarNumber\" : \"aadhaarNumber\",      \"createdDate\" : \"2000-01-23\",      \"createdBy\" : 7,      \"dob\" : \"2000-01-23\",      \"otpReference\" : \"otpReference\",      \"name\" : \"name\",      \"tenantId\" : \"tenantId\",      \"salutation\" : \"salutation\",      \"permanentCity\" : \"permanentCity\",      \"permanentPincode\" : \"permanentPincode\"    } ],    \"applicationType\" : \"applicationType\",    \"numberOfPersons\" : 1,    \"address\" : {      \"pincode\" : \"pincode\",      \"city\" : \"city\",      \"latitude\" : 7.061401241503109,      \"tenantId\" : \"tenantId\",      \"addressNumber\" : \"addressNumber\",      \"addressLine1\" : \"addressLine1\",      \"addressLine2\" : \"addressLine2\",      \"detail\" : \"detail\",      \"type\" : \"type\",      \"landmark\" : \"landmark\",      \"longitude\" : 9.301444243932576,      \"addressId\" : \"addressId\"    },    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"auditDetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 5      },      \"tenantId\" : \"tenantId\",      \"id\" : \"id\",      \"isActive\" : true    }, {      \"documentType\" : \"documentType\",      \"auditDetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 5      },      \"tenantId\" : \"tenantId\",      \"id\" : \"id\",      \"isActive\" : true    } ],    \"meter\" : {      \"connectionNumber\" : \"connectionNumber\",      \"meterOwner\" : \"ULB\",      \"meterSlNo\" : 7,      \"auditDetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 5      },      \"tenantId\" : \"tenantId\",      \"meterCost\" : 4,      \"id\" : \"id\",      \"meterModel\" : \"RFID\"    },    \"type\" : \"TEMPORARY\",    \"uuid\" : \"uuid\",    \"oldConnectionNumber\" : \"oldConnectionNumber\",    \"numberOfTaps\" : 6,    \"connectionNumber\" : \"connectionNumber\",    \"billingType\" : { },    \"sourceType\" : \"GroundWater\",    \"auditDetails\" : {      \"lastModifiedTime\" : 5,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 5    },    \"tenantId\" : \"tenantId\",    \"property\" : {      \"oldPropertyId\" : 2.3021358869347654518833223846741020679473876953125,      \"address\" : \"address\",      \"adharNumber\" : \"adharNumber\",      \"nameOfApplicant\" : \"nameOfApplicant\",      \"mobileNumber\" : \"mobileNumber\",      \"locality\" : \"locality\",      \"propertyId\" : \"propertyId\",      \"email\" : \"email\"    },    \"aditionalDetails\" : \"{}\",    \"pipesize\" : \"pipesize\",    \"location\" : {      \"buildingName\" : \"buildingName\",      \"gisNumber\" : \"gisNumber\",      \"locationBoundary\" : {        \"code\" : \"code\",        \"name\" : \"name\",        \"id\" : 2      },      \"auditDetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 5      },      \"id\" : 3,      \"billingAddress\" : \"billingAddress\",      \"revenueBoundary\" : {        \"code\" : \"code\",        \"name\" : \"name\",        \"id\" : 2      },      \"roadName\" : \"roadName\",      \"adminBoundary\" : {        \"code\" : \"code\",        \"name\" : \"name\",        \"id\" : 2      }    },    \"acknowledgmentNumber\" : \"acknowledgmentNumber\",    \"parentConnection\" : \"parentConnection\",    \"status\" : \"ACTIVE\"  }, {    \"owner\" : [ {      \"pwdExpiryDate\" : \"2000-01-23\",      \"correspondenceCity\" : \"correspondenceCity\",      \"gender\" : \"gender\",      \"signature\" : \"signature\",      \"mobileNumber\" : \"mobileNumber\",      \"roles\" : [ {        \"code\" : \"code\",        \"createdDate\" : \"2000-01-23\",        \"createdBy\" : 1,        \"lastModifiedDate\" : \"2000-01-23\",        \"lastModifiedBy\" : 6,        \"name\" : \"name\",        \"tenantId\" : \"tenantId\",        \"description\" : \"description\",        \"id\" : 1      }, {        \"code\" : \"code\",        \"createdDate\" : \"2000-01-23\",        \"createdBy\" : 1,        \"lastModifiedDate\" : \"2000-01-23\",        \"lastModifiedBy\" : 6,        \"name\" : \"name\",        \"tenantId\" : \"tenantId\",        \"description\" : \"description\",        \"id\" : 1      } ],      \"correspondencePincode\" : \"correspondencePincode\",      \"emailId\" : \"emailId\",      \"locale\" : \"locale\",      \"type\" : \"type\",      \"correspondenceAddress\" : \"correspondenceAddress\",      \"bloodGroup\" : \"bloodGroup\",      \"password\" : \"password\",      \"id\" : 1,      \"permanentAddress\" : \"permanentAddress\",      \"pan\" : \"pan\",      \"accountLocked\" : true,      \"altContactNumber\" : \"altContactNumber\",      \"identificationMark\" : \"identificationMark\",      \"lastModifiedDate\" : \"2000-01-23\",      \"lastModifiedBy\" : 1,      \"fatherOrHusbandName\" : \"fatherOrHusbandName\",      \"active\" : true,      \"photo\" : \"photo\",      \"userName\" : \"userName\",      \"aadhaarNumber\" : \"aadhaarNumber\",      \"createdDate\" : \"2000-01-23\",      \"createdBy\" : 7,      \"dob\" : \"2000-01-23\",      \"otpReference\" : \"otpReference\",      \"name\" : \"name\",      \"tenantId\" : \"tenantId\",      \"salutation\" : \"salutation\",      \"permanentCity\" : \"permanentCity\",      \"permanentPincode\" : \"permanentPincode\"    }, {      \"pwdExpiryDate\" : \"2000-01-23\",      \"correspondenceCity\" : \"correspondenceCity\",      \"gender\" : \"gender\",      \"signature\" : \"signature\",      \"mobileNumber\" : \"mobileNumber\",      \"roles\" : [ {        \"code\" : \"code\",        \"createdDate\" : \"2000-01-23\",        \"createdBy\" : 1,        \"lastModifiedDate\" : \"2000-01-23\",        \"lastModifiedBy\" : 6,        \"name\" : \"name\",        \"tenantId\" : \"tenantId\",        \"description\" : \"description\",        \"id\" : 1      }, {        \"code\" : \"code\",        \"createdDate\" : \"2000-01-23\",        \"createdBy\" : 1,        \"lastModifiedDate\" : \"2000-01-23\",        \"lastModifiedBy\" : 6,        \"name\" : \"name\",        \"tenantId\" : \"tenantId\",        \"description\" : \"description\",        \"id\" : 1      } ],      \"correspondencePincode\" : \"correspondencePincode\",      \"emailId\" : \"emailId\",      \"locale\" : \"locale\",      \"type\" : \"type\",      \"correspondenceAddress\" : \"correspondenceAddress\",      \"bloodGroup\" : \"bloodGroup\",      \"password\" : \"password\",      \"id\" : 1,      \"permanentAddress\" : \"permanentAddress\",      \"pan\" : \"pan\",      \"accountLocked\" : true,      \"altContactNumber\" : \"altContactNumber\",      \"identificationMark\" : \"identificationMark\",      \"lastModifiedDate\" : \"2000-01-23\",      \"lastModifiedBy\" : 1,      \"fatherOrHusbandName\" : \"fatherOrHusbandName\",      \"active\" : true,      \"photo\" : \"photo\",      \"userName\" : \"userName\",      \"aadhaarNumber\" : \"aadhaarNumber\",      \"createdDate\" : \"2000-01-23\",      \"createdBy\" : 7,      \"dob\" : \"2000-01-23\",      \"otpReference\" : \"otpReference\",      \"name\" : \"name\",      \"tenantId\" : \"tenantId\",      \"salutation\" : \"salutation\",      \"permanentCity\" : \"permanentCity\",      \"permanentPincode\" : \"permanentPincode\"    } ],    \"applicationType\" : \"applicationType\",    \"numberOfPersons\" : 1,    \"address\" : {      \"pincode\" : \"pincode\",      \"city\" : \"city\",      \"latitude\" : 7.061401241503109,      \"tenantId\" : \"tenantId\",      \"addressNumber\" : \"addressNumber\",      \"addressLine1\" : \"addressLine1\",      \"addressLine2\" : \"addressLine2\",      \"detail\" : \"detail\",      \"type\" : \"type\",      \"landmark\" : \"landmark\",      \"longitude\" : 9.301444243932576,      \"addressId\" : \"addressId\"    },    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"auditDetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 5      },      \"tenantId\" : \"tenantId\",      \"id\" : \"id\",      \"isActive\" : true    }, {      \"documentType\" : \"documentType\",      \"auditDetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 5      },      \"tenantId\" : \"tenantId\",      \"id\" : \"id\",      \"isActive\" : true    } ],    \"meter\" : {      \"connectionNumber\" : \"connectionNumber\",      \"meterOwner\" : \"ULB\",      \"meterSlNo\" : 7,      \"auditDetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 5      },      \"tenantId\" : \"tenantId\",      \"meterCost\" : 4,      \"id\" : \"id\",      \"meterModel\" : \"RFID\"    },    \"type\" : \"TEMPORARY\",    \"uuid\" : \"uuid\",    \"oldConnectionNumber\" : \"oldConnectionNumber\",    \"numberOfTaps\" : 6,    \"connectionNumber\" : \"connectionNumber\",    \"billingType\" : { },    \"sourceType\" : \"GroundWater\",    \"auditDetails\" : {      \"lastModifiedTime\" : 5,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 5    },    \"tenantId\" : \"tenantId\",    \"property\" : {      \"oldPropertyId\" : 2.3021358869347654518833223846741020679473876953125,      \"address\" : \"address\",      \"adharNumber\" : \"adharNumber\",      \"nameOfApplicant\" : \"nameOfApplicant\",      \"mobileNumber\" : \"mobileNumber\",      \"locality\" : \"locality\",      \"propertyId\" : \"propertyId\",      \"email\" : \"email\"    },    \"aditionalDetails\" : \"{}\",    \"pipesize\" : \"pipesize\",    \"location\" : {      \"buildingName\" : \"buildingName\",      \"gisNumber\" : \"gisNumber\",      \"locationBoundary\" : {        \"code\" : \"code\",        \"name\" : \"name\",        \"id\" : 2      },      \"auditDetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 5      },      \"id\" : 3,      \"billingAddress\" : \"billingAddress\",      \"revenueBoundary\" : {        \"code\" : \"code\",        \"name\" : \"name\",        \"id\" : 2      },      \"roadName\" : \"roadName\",      \"adminBoundary\" : {        \"code\" : \"code\",        \"name\" : \"name\",        \"id\" : 2      }    },    \"acknowledgmentNumber\" : \"acknowledgmentNumber\",    \"parentConnection\" : \"parentConnection\",    \"status\" : \"ACTIVE\"  } ]}", WaterConnectionRes.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<WaterConnectionRes>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<WaterConnectionRes>(HttpStatus.NOT_IMPLEMENTED);
                }

        }