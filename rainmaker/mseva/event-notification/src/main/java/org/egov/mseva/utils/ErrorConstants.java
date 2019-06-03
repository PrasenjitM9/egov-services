package org.egov.mseva.utils;

import org.springframework.stereotype.Component;

@Component
public class ErrorConstants {
	
	public static final String MISSING_ROLE_USERID_CODE = "MEN_USERID_ROLECODE_MISSING";
	public static final String MISSING_ROLE_USERID_MSG = "User id and rolecode are mandatory in the request info";
	
	public static final String MISSING_REQ_INFO_CODE = "MEN_REQ_INFO_MISSING";
	public static final String MISSING_REQ_INFO_MSG = "RequestInfo is mandatory in the request.";
	
	public static final String EMPTY_RECEPIENT_CODE = "MEN_EMPTY_RECEPIENT";
	public static final String EMPTY_RECEPIENT_MSG = "toRoles and toUsers cannot be empty!";
	
	public static final String INVALID_EVENT_DATE_CODE = "MEN_INVALID_EVENT_DATE";
	public static final String INVALID_EVENT_DATE_MSG = "Date invalid, fromDate cannot be greater than toDate";
	
	public static final String INVALID_FROM_TO_DATE_CODE = "MEN_INVALID_FROM_TO_DATE";
	public static final String INVALID_FROM_TO_DATE_MSG = "Date invalid, fromDate and toDate cannot be greater than currentDate";

}
