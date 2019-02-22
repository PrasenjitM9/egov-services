package org.egov.user.encryption.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;

@Slf4j
public class JacksonUtils {

    public static JsonNode merge(JsonNode newNode, JsonNode originalNode) {
        Iterator<String> fieldNames = originalNode.fieldNames();

        while (fieldNames.hasNext()) {

            String fieldName = fieldNames.next();
            JsonNode jsonNode = newNode.get(fieldName);

            if (jsonNode != null) {
                if (jsonNode.isObject()) {
                    merge(jsonNode, originalNode.get(fieldName));
                } else if (jsonNode.isArray()) {
                    for (int i = 0; i < jsonNode.size(); i++) {
                        merge(jsonNode.get(i), originalNode.get(fieldName).get(i));
                    }
                }
            } else {
                if (newNode instanceof ObjectNode) {
                    // Overwrite field
                    JsonNode value = originalNode.get(fieldName);

                    ((ObjectNode) newNode).set(fieldName, value);
                }
            }
        }

        return newNode;
    }

    public static JsonNode filterJsonNodeWithFields(JsonNode jsonNode, List<String> filterFields) {
        if(checkIfNoFieldExistsInJsonNode(jsonNode, filterFields))
            return null;

        ObjectMapper mapper = new ObjectMapper(new JsonFactory());


        if(jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            ObjectNode filteredObjectNode = mapper.createObjectNode();
            Iterator<String> fieldIterator = objectNode.fieldNames();
            while (fieldIterator.hasNext()) {
                String field = fieldIterator.next();
                if(filterFields.contains(field) && !objectNode.get(field).isNull()) {
                    filteredObjectNode.set(field, objectNode.get(field));
                } else {
                    JsonNode filteredJsonNode = filterJsonNodeWithFields(objectNode.get(field), filterFields);
                    if(filteredJsonNode != null) {
                        filteredObjectNode.set(field, filteredJsonNode);
                    }
                }
            }
            if(filteredObjectNode.isEmpty(mapper.getSerializerProvider()))
                return null;
            return filteredObjectNode;
        } else if(jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            ArrayNode filteredArrayNode = mapper.createArrayNode();
            for(int i = 0; i < arrayNode.size(); i++) {
                JsonNode filteredJsonNode = filterJsonNodeWithFields(arrayNode.get(i), filterFields);
                if(filteredJsonNode == null) {
                    if(arrayNode.get(i).isArray())
                        filteredJsonNode = mapper.createArrayNode();
                    else if(arrayNode.get(i).isObject())
                        filteredJsonNode = mapper.createObjectNode();
                    else
                        filteredJsonNode = NullNode.getInstance();
                }
                filteredArrayNode.add(filteredJsonNode);
            }
            return filteredArrayNode;
        }

        return null;
    }


    public static boolean checkIfNoFieldExistsInJsonNode(JsonNode jsonNode, List<String> fields) {
        for(String field : fields) {
            if(! String.valueOf(jsonNode.findPath(field)).isEmpty())
                return false;
        }
        return true;
    }

}
