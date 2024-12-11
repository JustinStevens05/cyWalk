package com.cywalk.spring_boot.Organizations;

import com.cywalk.spring_boot.Users.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


// Ethan Ashihundu TESTS

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class OrganizationSystemTest {

    @LocalServerPort
    int port;

    private final String BASE_URL = "http://localhost";

    private static Long keyBase;

    private static final String USER_ONE = "testOrg";
    private static final String BASE_USER = "uhhhhOrg";
    private static final String TEST_ORG = "TestOrg";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserModelRepository userModelRepository;

    @Autowired
    private UserRequestRepository userRequestRepository;

    @Autowired
    private PeopleRepository peopleRepository;

    @Before
    @Order(1)
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.port = port;
    }

    @Test
    @Transactional
    @Order(2)
    void cleanupExistingUsers() {
        if (peopleService.getUserByUsername(USER_ONE).isPresent()) {
            peopleService.deleteUserByName(USER_ONE);
        }
        if (peopleService.getUserByUsername(BASE_USER).isPresent()) {
            peopleService.deleteUserByName(BASE_USER);
        }
        if (organizationService.getOrganizationIdByName(TEST_ORG).isPresent()) {
            organizationRepository.deleteById(organizationService.getOrganizationIdByName(TEST_ORG).get());
        }

    }

    @Test
    @Order(3)
    @Transactional
    void signUpUsersAndOrganizations() {
        setup();
        cleanupExistingUsers();

        Response baseSignup = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createPersonRequest(BASE_USER)))
                .post("/signup");

        assertEquals(200, baseSignup.getStatusCode());
        keyBase = extractKeyFromResponse(baseSignup);

        Response testSignup = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createPersonRequest(USER_ONE)))
                .post("/signup");
        assertEquals(200, testSignup.getStatusCode());
        keyBase = extractKeyFromResponse(testSignup);

        Response testOrg = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createOrgRequest(TEST_ORG)))
                .post("/organizations");
        assertEquals(200, testOrg.getStatusCode());
    }

    @Test
    @Order(4)
    @Transactional
    void handleOrganizationMembership() {

        long id = organizationService.getOrganizationIdByName(TEST_ORG).get();
        Response joinRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createJoinRequest(USER_ONE)))
                .post("/organizations/" + id + "/join");
        assertEquals(200, joinRequest.getStatusCode());

        Response listUsers = RestAssured.given()
                .header("Content-Type", "application/json")
                .get("/organizations/" + id + "/users");
        assertEquals(200, listUsers.getStatusCode());
        assertTrue(listUsers.getBody().asString().contains(USER_ONE));

        Response getInfo = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createUsernameRequest(USER_ONE)))
                .post("/organizations/get-info");
        assertEquals(200, getInfo.getStatusCode());
        assertTrue(getInfo.getBody().asString().contains(TEST_ORG));
    }

    private UserRequest createPersonRequest(String username) {
        UserRequest request = new UserRequest();
        request.setUsername(username);
        request.setPassword("password");
        return request;
    }

    private CreateOrganizationRequest createOrgRequest(String name) {
        CreateOrganizationRequest request = new CreateOrganizationRequest();
        request.setName(name);
        return request;
    }

    private JoinOrganizationRequest createJoinRequest(String username) {
        JoinOrganizationRequest request = new JoinOrganizationRequest();
        request.setUsername(username);
        return request;
    }
    private Object createUsernameRequest(String username) {
        return java.util.Collections.singletonMap("username", username);
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Long extractKeyFromResponse(Response response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody().asString());
            return root.path("id").asLong();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}