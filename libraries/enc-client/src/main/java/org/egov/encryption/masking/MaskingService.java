package org.egov.encryption.masking;

import com.fasterxml.jackson.databind.JsonNode;
import org.egov.encryption.models.Attribute;
import org.egov.encryption.util.JSONBrowseUtil;
import org.egov.encryption.util.JacksonUtils;
import org.reflections.Reflections;

import java.util.*;

public class MaskingService {

    Map<String, Masking> maskingTechniqueMap;

    private void init() throws IllegalAccessException, InstantiationException {
        maskingTechniqueMap = new HashMap<>();

        Reflections reflections = new Reflections(getClass().getPackage().getName());
        Set<Class<? extends Masking>> maskingTechniques =  reflections.getSubTypesOf(Masking.class);
        for(Class<? extends Masking> maskingTechnique : maskingTechniques) {
            Masking masking = maskingTechnique.newInstance();
            maskingTechniqueMap.put(masking.getMaskingTechnique(), masking);
        }
    }

    public MaskingService() throws InstantiationException, IllegalAccessException {
        init();
    }

    public String maskData(String data, Attribute attribute) {
        Masking masking = maskingTechniqueMap.get(attribute.getMaskingTechnique());

        return masking.maskData(data);
    }

    public JsonNode maskData(JsonNode decryptedNode, List<Attribute> attributes) {
        JsonNode maskedNode = decryptedNode.deepCopy();

        for(Attribute attribute : attributes) {
            Masking masking = maskingTechniqueMap.get(attribute.getMaskingTechnique());

            JsonNode jsonNode = JacksonUtils.filterJsonNodeWithPaths(maskedNode, Arrays.asList(attribute.getJsonPath()));

            jsonNode = JSONBrowseUtil.mapValues(jsonNode, value -> masking.maskData(value));

            maskedNode = JacksonUtils.merge(jsonNode, maskedNode);
        }

        return maskedNode;
    }

}