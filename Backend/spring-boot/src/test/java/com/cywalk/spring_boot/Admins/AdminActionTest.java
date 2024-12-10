package com.cywalk.spring_boot.Admins;

import com.cywalk.spring_boot.Organizations.JoinOrganizationRequest;
import com.cywalk.spring_boot.Organizations.OrganizationRepository;
import com.cywalk.spring_boot.Organizations.OrganizationService;
import com.cywalk.spring_boot.Users.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.annotation.Order;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminActionTest {
    /*
    @Autowired
    private PeopleService peopleService;
     */

    @LocalServerPort
    int port;

    private final String BASE_URL = "http://localhost";

    private static Long keyBase;

    private static final String USER_ONE = "userOne";
    private static final String user =  "jimmothy";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private AdminRepository AdminRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminCredentialRepository adminCredentialRepository;

    @Autowired
    private AdminSessionRepository adminSessionRepository;
    @Autowired
    private OrganizationService organizationService;

    @Before
    @Order(1)
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.port = port;
    }


    @Test
    @Order(2)
    @Transactional
    void removeUserFromOrg() throws JSONException {
        setup();


        // Sign up test user
        Response testSignup = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createAdminRequest(USER_ONE)))
                .post("/signup/organization");
        assertEquals(200, testSignup.getStatusCode());
        long keyTest = extractKeyFromResponse(testSignup);

        // sign up model user to organization
        Response testSignup2 = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createUserRequest(user)))
                .post("/signup");
        assertEquals(200, testSignup2.getStatusCode());
        double userKey = extractKeyFromResponse(testSignup2);

        // make a request to get the organization ID
        /*
        Had this request in there but I had absolutely no idea how to format this request without a proper model. Idk what Map<String, String> is supposed to be
        Response orgId = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(findOrgIdRequest("ISU"))
                .post("/organizations/find");
        assertEquals(200, orgId.getStatusCode());
         */
        long id = organizationService.getOrganizationIdByName("ISU").get();

        // make a request for user to join organization: ISU
        Response joinOrg = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createJoinRequest(user)))
                .post("/organizations/" + id + "/join");
        assertEquals(200, joinOrg.getStatusCode());

        // make a request that verifies the user is in the organization
        Response listUsers = RestAssured.given()
                .header("Content-Type", "application/json")
                .get("/organizations/" + id + "/users");
        assertEquals(200, listUsers.getStatusCode());
        assertTrue(listUsers.getBody().asString().contains(user));

        // make a request that has admin: USER_ONE remove user: user from organization: ISU
        Response removeUser = RestAssured.given()
                .header("Content-Type", "application/json")
                .delete("/organizations/remove/" + keyTest + "/" + user);
        assertEquals(200, removeUser.getStatusCode());

        // make a request that verifies the user is not in the organization
        Response listUsers2 = RestAssured.given()
                .header("Content-Type", "application/json")
                .get("/organizations/" + keyTest + "/users");
        assertEquals(200, listUsers2.getStatusCode());
        assertFalse(listUsers2.getBody().asString().contains(user));
    }

    private JSONObject findOrgIdRequest(String orgname) throws JSONException {
        JSONObject request = new JSONObject();
        request.put("name", orgname);
        return request;
    }

    private AdminOrganizationCredModel createAdminRequest(String username) {
        AdminOrganizationCredModel request = new AdminOrganizationCredModel();
        request.setAdminName(username);
        request.setOrganizationName("ISU");
        request.setPassword("password");
        return request;
    }

    private JoinOrganizationRequest createJoinRequest(String username) {
        JoinOrganizationRequest joinOrganizationRequest = new JoinOrganizationRequest();
        joinOrganizationRequest.setUsername(username);
        return joinOrganizationRequest;
    }

    private UserRequest createUserRequest(String username) {
        UserRequest request = new UserRequest();
        request.setUsername(username);
        request.setPassword("pass");
        return request;
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
            return root.path("key").asLong();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private long extractIdFromResponse(Response response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody().asString());
            return root.path("id").asLong();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
