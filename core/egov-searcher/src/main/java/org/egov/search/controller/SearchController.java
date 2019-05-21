package org.egov.search.controller;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.egov.search.model.SearchRequest;
import org.egov.search.service.SearchService;
import org.egov.search.utils.SearchReqValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
public class SearchController {
		
	@Autowired
	private SearchService searchService;
	
	@Autowired
	private SearchReqValidator searchReqValidator;
		
	@Autowired
    public static ResourceLoader resourceLoader;
	
	public static final Logger logger = LoggerFactory.getLogger(SearchController.class);

	@PostMapping("/{moduleName}/{searchName}/_get")
	@ResponseBody
	public ResponseEntity<?> getReportData(@RequestParam HashMap<String,String> allRequestParams, @PathVariable("moduleName") String moduleName,
										   @PathVariable("searchName") String searchName,
										   @RequestBody  final SearchRequest searchRequest) {
		long startTime = new Date().getTime();
		try
		{
			HashMap<String,String>searchcriteria=(HashMap<String,String>)searchRequest.getSearchCriteria();
			if(searchcriteria!=null)
			{
				searchcriteria.putAll(allRequestParams);
			}
			else if(allRequestParams.keySet().size()>0){
				searchRequest.setSearchCriteria(allRequestParams);
			}

			searchReqValidator.validate(searchRequest, moduleName, searchName);
			Object searchResult = searchService.searchData(searchRequest,moduleName,searchName);
		    Type type = new TypeToken<Map<String, Object>>() {}.getType();
			Gson gson = new Gson();
			Map<String, Object> data = gson.fromJson(searchResult.toString(), type);
			long endTime = new Date().getTime();
			logger.info(" the time taken for search in controller in ms : "+(endTime-startTime));
			return new ResponseEntity<>(data, HttpStatus.OK);
		} catch(Exception e){
			logger.error("Exception while searching for result: ",e);
				throw e;
		}
	}

		
}