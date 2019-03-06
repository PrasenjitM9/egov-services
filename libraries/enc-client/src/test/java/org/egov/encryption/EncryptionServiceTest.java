package org.egov.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.encryption.accesscontrol.AbacFilter;
import org.egov.encryption.models.RoleAttributeAccess;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class EncryptionServiceTest {

    ObjectMapper mapper;
    Configuration configuration;
    EncryptionService encryptionService;
    User user;

    @Before
    public void init() throws InstantiationException, IllegalAccessException, IOException {
        Role role1 = Role.builder().code("GRO").build();
        Role role2 = Role.builder().code("LME").build();
        user = User.builder().roles(Arrays.asList(role1, role2)).build();

        mapper = new ObjectMapper(new JsonFactory());
        configuration = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS);

        Map<String, String> fieldsAndTheirType = new HashMap<>();
        fieldsAndTheirType.put("User/mobileNumber", "Normal");
        fieldsAndTheirType.put("User/name", "Normal");
        fieldsAndTheirType.put("User/userName", "Normal");

        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        ObjectReader reader = objectMapper.readerFor(objectMapper.getTypeFactory().constructCollectionType(List.class,
                RoleAttributeAccess.class));

        URL url = getClass().getClassLoader().getResource("RoleAttributeAccessList.json");
        String roleAttributeAccessListString = new String(Files.readAllBytes(Paths.get(url.getPath())));
        List<RoleAttributeAccess> roleAttributeAccessList = reader.readValue(roleAttributeAccessListString);

        encryptionService = new EncryptionService(fieldsAndTheirType, roleAttributeAccessList);
    }

    @Ignore
    @Test
    public void encryptJsonObject() throws IOException {
        JsonNode plaintext = mapper.readTree("{\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\",\"ts\":null," +
                "\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null},\"User\":{\"userName\":\"ajay\",\"name\":\"ajay\",\"gender\":\"male\"," +
                "\"mobileNumber\":\"12312312\",\"active\":true,\"type\":\"CITIZEN\",\"password\":\"password\"}}");

        JsonNode ciphertext = encryptionService.encryptJson(plaintext, "pb");
        log.info(ciphertext.toString());
    }

    @Ignore
    @Test
    public void decryptJsonObject() throws IOException {
        JsonNode ciphertext = mapper.readTree("{\"User\":{\"mobileNumber\":\"341642|WfYfJPRug15R2wFh17PlQr5d9YhNkFk1" +
                "\",\"name\":\"341642|Ca5NbGHu3aB2ufjrNfZarW1VGBA=\"," +
                "\"userName\":\"341642|Ca5NbGHu3aB2ufjrNfZarW1VGBA=\",\"gender\":\"male\",\"active\":true," +
                "\"type\":\"CITIZEN\",\"password\":\"password\"},\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\"," +
                "\"ts\":null,\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null}}");

        JsonNode plaintext = encryptionService.decryptJson(ciphertext, Arrays.asList("User/mobileNumber", "User" +
                "/name"), User.builder().build());
        log.info(plaintext.toString());
    }

    @Ignore
    @Test
    public void decryptJsonArray() throws IOException {
        JsonNode ciphertext = mapper.readTree("[{\"User\":{\"mobileNumber\":\"341642|WfYfJPRug15R2wFh17PlQr5d9YhNkFk1" +
                "\",\"name\":\"341642|Ca5NbGHu3aB2ufjrNfZarW1VGBA=\"," +
                "\"userName\":\"341642|Ca5NbGHu3aB2ufjrNfZarW1VGBA=\",\"gender\":\"male\",\"active\":true," +
                "\"type\":\"CITIZEN\",\"password\":\"password\"},\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\"," +
                "\"ts\":null,\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null}}]");

        JsonNode plaintext = encryptionService.decryptJson(ciphertext, Arrays.asList("*/User/mobileNumber", "*/User" +
                "/name"), User.builder().build());
        log.info(plaintext.toString());
    }

    @Ignore
    @Test
    public void decryptJsonUsingRoles() throws IOException {
        JsonNode ciphertext = mapper.readTree("[{\"User\":{\"mobileNumber\":\"341642|WfYfJPRug15R2wFh17PlQr5d9YhNkFk1" +
                "\",\"name\":\"341642|Ca5NbGHu3aB2ufjrNfZarW1VGBA=\"," +
                "\"userName\":\"341642|Ca5NbGHu3aB2ufjrNfZarW1VGBA=\",\"gender\":\"male\",\"active\":true," +
                "\"type\":\"CITIZEN\",\"password\":\"password\"},\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\"," +
                "\"ts\":null,\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null}}]");

        JsonNode plaintext = encryptionService.decryptJson(ciphertext, user);
        log.info(plaintext.toString());
    }


    @Test
    public void test() throws IOException {

    }



}