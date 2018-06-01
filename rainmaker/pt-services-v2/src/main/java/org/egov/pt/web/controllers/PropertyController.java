package org.egov.pt.web.controllers;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.pt.service.PropertyService;
import org.egov.pt.util.ResponseInfoFactory;
import org.egov.pt.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/property")
public class PropertyController {

	@Autowired
	private PropertyService propertyService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@RequestMapping(value = "/_create", method = RequestMethod.POST)
	public ResponseEntity<PropertyResponse> create(@Valid @RequestBody PropertyRequest propertyRequest) {

		List<Property> properties = propertyService.createProperty(propertyRequest);
		PropertyResponse response = PropertyResponse.builder().properties(properties)
				.responseInfo(
						responseInfoFactory.createResponseInfoFromRequestInfo(propertyRequest.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/_update", method = RequestMethod.POST)
	public ResponseEntity<PropertyResponse> update(@Valid @RequestBody PropertyRequest propertyRequest) {

		List<Property> properties = propertyService.updateProperty(propertyRequest);
		PropertyResponse response = PropertyResponse.builder().properties(properties)
				.responseInfo(
						responseInfoFactory.createResponseInfoFromRequestInfo(propertyRequest.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/_search", method = RequestMethod.POST)
	public ResponseEntity<PropertyResponse> search(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute PropertyCriteria propertyCriteria) {

		List<Property> properties = propertyService.searchProperty(propertyCriteria);
		PropertyResponse response = PropertyResponse.builder().properties(properties).responseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
