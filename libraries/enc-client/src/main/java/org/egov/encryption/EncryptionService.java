package org.egov.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.User;
import org.egov.encryption.accesscontrol.AbacFilter;
import org.egov.encryption.models.AccessType;
import org.egov.encryption.models.Attribute;
import org.egov.encryption.models.Role;
import org.egov.encryption.util.ConvertClass;
import org.egov.encryption.util.JacksonUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class EncryptionService {

    private EncryptionServiceRestInterface encryptionServiceRestInterface;

    private AbacFilter abacFilter;

    private Map<String, String> fieldsAndTheirType;

    private Map<String, List<String>> typesAndFieldsToEncrypt;

    private ObjectMapper mapper;


    public EncryptionService(Map<String, String> fieldsAndTheirType) {
//        this.abacFilter = abacFilter;
        this.fieldsAndTheirType = fieldsAndTheirType;
        encryptionServiceRestInterface = new EncryptionServiceRestInterface();
        mapper = new ObjectMapper(new JsonFactory());
        initializeTypesAndFieldsToEncrypt();
    }

    void initializeTypesAndFieldsToEncrypt() {
        typesAndFieldsToEncrypt = new HashMap<>();
        for (String field : fieldsAndTheirType.keySet()) {
            String type = fieldsAndTheirType.get(field);
            if (!typesAndFieldsToEncrypt.containsKey(type)) {
                List<String> fieldsToEncrypt = new ArrayList<String>();
                typesAndFieldsToEncrypt.put(type, fieldsToEncrypt);
            }
            typesAndFieldsToEncrypt.get(type).add(field);
        }
    }

    public JsonNode encryptJson(Object plaintextJson, String tenantId) throws IOException {

        JsonNode plaintextNode = createJsonNode(plaintextJson);
        JsonNode encryptedNode = plaintextNode.deepCopy();

        for (String type : typesAndFieldsToEncrypt.keySet()) {
            List<String> fields = typesAndFieldsToEncrypt.get(type);

            JsonNode jsonNode = JacksonUtils.filterJsonNodeWithPaths(plaintextNode, fields);

            if(! jsonNode.isEmpty(mapper.getSerializerProvider())) {
                JsonNode returnedEncryptedNode = mapper.valueToTree(encryptionServiceRestInterface.callEncrypt(tenantId,
                        type, jsonNode));
                encryptedNode = JacksonUtils.merge(returnedEncryptedNode, encryptedNode);
            }
        }

        return encryptedNode;
    }

    public <T> T encryptJson(Object plaintextJson, String tenantId, Class<T> valueType) throws IOException {
        return ConvertClass.convertTo(encryptJson(plaintextJson, tenantId), valueType);
    }

    public JsonNode decryptJson(Object ciphertextJson, List<String> paths, User user) throws IOException {
        JsonNode ciphertextNode = createJsonNode(ciphertextJson);
        JsonNode decryptedNode = ciphertextNode.deepCopy();

        JsonNode jsonNode = JacksonUtils.filterJsonNodeWithPaths(ciphertextNode, paths);
        if(! jsonNode.isEmpty(mapper.getSerializerProvider())) {
            JsonNode returnedDecryptedNode = mapper.valueToTree(encryptionServiceRestInterface.callDecrypt(jsonNode));
            decryptedNode = JacksonUtils.merge(returnedDecryptedNode, decryptedNode);
        }

        return decryptedNode;
    }

    public <T> T decryptJson(Object ciphertextJson, List<String> paths, User user, Class<T> valueType) throws IOException {
        return ConvertClass.convertTo(decryptJson(ciphertextJson, paths, user), valueType);
    }


    public JsonNode decryptJson(Object ciphertextJson, User user) throws IOException {

        List<Role> roles = user.getRoles().stream().map(Role::new).collect(Collectors.toList());

        Map<Attribute, AccessType> attributeAccessTypeMap = abacFilter.getAttributeAccessForRoles(roles);
        List<String> paths = attributeAccessTypeMap.keySet().stream()
                .map(Attribute::getJsonPath).collect(Collectors.toList());

        JsonNode decryptedNode = decryptJson(ciphertextJson, paths, user);

        return decryptedNode;
    }

    public <T> T decryptJson(Object ciphertextJson, User user, Class<T> valueType) throws IOException {
        return ConvertClass.convertTo(decryptJson(ciphertextJson, user), valueType);
    }

    JsonNode createJsonNode(Object json) throws IOException {
        JsonNode jsonNode;
        if(json instanceof JsonNode)
            jsonNode = (JsonNode) json;
        else if(json instanceof String)
            jsonNode = mapper.readTree((String) json);           //JsonNode from JSON String
        else
            jsonNode = mapper.valueToTree(json);                 //JsonNode from POJO or Map
        return jsonNode;
    }


    String encryptValue(String plaintext, String tenantId, String type) throws IOException {
        return encryptValue(new ArrayList<String>(Collections.singleton(plaintext)), tenantId, type).get(0);
    }

    List<String> encryptValue(List<String> plaintext, String tenantId, String type) throws IOException {
        Object encryptionResponse = encryptionServiceRestInterface.callEncrypt(tenantId, type, plaintext);
        return (List<String>) encryptionResponse;
    }

    Object decryptValue(Object ciphertext) throws IOException {
        return encryptionServiceRestInterface.callDecrypt(ciphertext);
    }


}