package org.egov.infra.mdms.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.mdms.model.MDMSCreateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Component
public class MDMSUtils {

	public static final Logger logger = LoggerFactory.getLogger(MDMSUtils.class);

	public List<Object> filter(List<Object> list, String key, Object value) throws JsonProcessingException {
		List<Object> filteredList = new ArrayList<>();
		if (list.size() == 1 && null == list.get(0))
			return filteredList;
		ObjectMapper mapper = new ObjectMapper();
		try {
			filteredList = list.parallelStream()
					.filter(obj -> true == ((JsonPath.read(obj.toString(), key.toString())).equals(value)))
					.collect(Collectors.toList());
		} catch (Exception e) {
			filteredList = list.parallelStream().map(obj -> {
				try {
					return mapper.writeValueAsString(obj);
				} catch (Exception ex) {
					logger.error("Parsing error inside the stream: ", ex);
				}
				return null;
			}).filter(obj -> true == ((JsonPath.read(obj, key.toString())).equals(value))).collect(Collectors.toList());
		}
		return filteredList;
	}

	public List<Object> filter(List<Object> list, Object arrayElement) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNodeInput = mapper.readTree(arrayElement.toString());
		logger.info("jsonNodeInput: " + jsonNodeInput);
		List<Object> filterResult = new ArrayList<>();
		filterResult = list.parallelStream().map(obj -> {
			try {
				String jsonStr = mapper.writeValueAsString(obj);
				JsonNode jsonNode = mapper.readTree(jsonStr);
				logger.info("jsonNode: " + jsonNode);
				return jsonNode;
			} catch (Exception ex) {
				logger.error("Parsing error inside the stream: ", ex);
			}
			return null;
		}).filter(obj -> true == (obj.equals(jsonNodeInput))).collect(Collectors.toList());
		logger.info("filterResult: " + filterResult);

		return filterResult;
	}

	/***
	 * To get the mdms config list when it comes from the Data folder itself
	 * 
	 * @param mDMSCreateRequest
	 * @param allMasters
	 * @return
	 * @throws Exception
	 */
	public List<String> getUniqueKeys(MDMSCreateRequest mDMSCreateRequest, Map<String, Object> allMasters)
			throws Exception {
		List<String> uniqueKeys = new ArrayList<>();
		// List<Object> allmasterConfigs = (List<Object>)
		// allMasters.get(MDMSConstants.CONFIG_ARRAY_KEY);
		if (null == allMasters || allMasters.isEmpty()) {
			logger.info("There is no mdms-config for this module: "
					+ mDMSCreateRequest.getMasterMetaData().getModuleName());
			return uniqueKeys;
		}
		List<Object> allmasterConfigs = new ArrayList<Object>();
		allmasterConfigs.add(allMasters.get(mDMSCreateRequest.getMasterMetaData().getMasterName()));
		List<Object> masterConfig = filter(allmasterConfigs, MDMSConstants.MASTERNAME_JSONPATH,
				mDMSCreateRequest.getMasterMetaData().getMasterName());
		if (masterConfig.size() > 1) {
			logger.info("There are duplicate mdms-configs for this master: "
					+ mDMSCreateRequest.getMasterMetaData().getMasterName());
			uniqueKeys = null;
			return uniqueKeys;
		} else if (masterConfig.isEmpty()) {
			logger.info("There is no mdms-config for this master: "
					+ mDMSCreateRequest.getMasterMetaData().getMasterName());
			return uniqueKeys;
		}
		uniqueKeys = JsonPath.read(masterConfig.get(0).toString(), MDMSConstants.UNIQUEKEYS_JSONPATH);

		return uniqueKeys;
	}
	
	/***
	 * To Get Mdms-config map when it comes from resources or external sources other than Data folder itself
	 * 
	 * @param masterConfigMap
	 * @param mDMSCreateRequest
	 * @return
	 */
	public List<String> getUniqueKeys(Map<String, Map<String, Object>> masterConfigMap, MDMSCreateRequest mDMSCreateRequest){
		
		logger.info(" entering getUniqueKeys : "+ masterConfigMap);
		List<String> uniqueKeys = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> moduleMap = masterConfigMap.get(mDMSCreateRequest.getMasterMetaData().getModuleName());
		if(null == moduleMap) {
			return uniqueKeys;
		}
		Object masterConfig = moduleMap.get(mDMSCreateRequest.getMasterMetaData().getMasterName());
		if(null == masterConfig) {
			return uniqueKeys;
		}
		Map<String, Object> masterConfigs = mapper.convertValue(masterConfig, Map.class);
		uniqueKeys = (List<String>) masterConfigs.get(MDMSConstants.UNIQUE_KEY);
		
		logger.info("uniqueKeys: "+uniqueKeys);
		
		return uniqueKeys;
	}

	public String getJsonPathKey(String jsonPath, StringBuilder expression) {
		String[] expressionArray = (jsonPath).split("[.]");
		for (int j = 0; j < (expressionArray.length - 1); j++) {
			expression.append(expressionArray[j]);
			if (j != expressionArray.length - 2)
				expression.append(".");
		}
		return expressionArray[expressionArray.length - 1];
	}

	public String buildCommitMessage(MDMSCreateRequest mDMSCreateRequest, boolean isCreate, String username) {
		StringBuilder commitMessage = new StringBuilder();
		if (null != mDMSCreateRequest.getRequestInfo().getUserInfo()) {
			if (null != mDMSCreateRequest.getRequestInfo().getUserInfo().getUserName()
					&& !mDMSCreateRequest.getRequestInfo().getUserInfo().getUserName().isEmpty()) {
				if (isCreate) {
					commitMessage
							.append("commit by " + mDMSCreateRequest.getRequestInfo().getUserInfo().getUserName() + " ")
							.append("to ADD masterdata ")
							.append("for module: " + mDMSCreateRequest.getMasterMetaData().getModuleName() + ", ")
							.append("master: " + mDMSCreateRequest.getMasterMetaData().getMasterName() + ", ")
							.append("tenant: " + mDMSCreateRequest.getMasterMetaData().getTenantId());
				} else {
					commitMessage
							.append("commit by " + mDMSCreateRequest.getRequestInfo().getUserInfo().getUserName() + " ")
							.append("to UPDATE masterdata ")
							.append("for module: " + mDMSCreateRequest.getMasterMetaData().getModuleName() + ", ")
							.append("master: " + mDMSCreateRequest.getMasterMetaData().getMasterName() + ", ")
							.append("tenant: " + mDMSCreateRequest.getMasterMetaData().getTenantId());
				}
			} else {
				if (isCreate) {
					commitMessage.append("commit by " + username + " ").append("to ADD masterdata ")
							.append("for module: " + mDMSCreateRequest.getMasterMetaData().getModuleName() + ", ")
							.append("master: " + mDMSCreateRequest.getMasterMetaData().getMasterName() + ", ")
							.append("tenant: " + mDMSCreateRequest.getMasterMetaData().getTenantId());
				} else {
					commitMessage.append("commit by " + username + " ").append("to UPDATE masterdata ")
							.append("for module: " + mDMSCreateRequest.getMasterMetaData().getModuleName() + ", ")
							.append("master: " + mDMSCreateRequest.getMasterMetaData().getMasterName() + ", ")
							.append("tenant: " + mDMSCreateRequest.getMasterMetaData().getTenantId());
				}
			}
		} else {
			if (isCreate) {
				commitMessage.append("commit by " + username + " ").append("to ADD masterdata ")
						.append("for module: " + mDMSCreateRequest.getMasterMetaData().getModuleName() + ", ")
						.append("master: " + mDMSCreateRequest.getMasterMetaData().getMasterName() + ", ")
						.append("tenant: " + mDMSCreateRequest.getMasterMetaData().getTenantId());
			} else {
				commitMessage.append("commit by " + username + " ").append("to UPDATE masterdata ")
						.append("for module: " + mDMSCreateRequest.getMasterMetaData().getModuleName() + ", ")
						.append("master: " + mDMSCreateRequest.getMasterMetaData().getMasterName() + ", ")
						.append("tenant: " + mDMSCreateRequest.getMasterMetaData().getTenantId());
			}
		}

		return commitMessage.toString();

	}

}
