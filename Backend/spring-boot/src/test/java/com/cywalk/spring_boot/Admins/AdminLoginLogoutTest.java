package com.cywalk.spring_boot.Admins;

import com.cywalk.spring_boot.Organizations.JoinOrganizationRequest;
import com.cywalk.spring_boot.Organizations.OrganizationRepository;
import com.cywalk.spring_boot.Organizations.OrganizationService;
import com.cywalk.spring_boot.Users.PeopleService;
import com.cywalk.spring_boot.Users.UserRequest;
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
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class AdminLoginLogoutTest {
    /*
    @Autowired
    private PeopleService peopleService;
     */

    @LocalServerPort
    int port;

    private final String BASE_URL = "http://localhost";

    private static Long keyBase;

    private static final String USER_ONE = "user2";
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
        long keyTest = extractIdFromResponse(testSignup);

        Response logout = RestAssured.given()
                .header("Content-Type", "application/json")
                .delete("/admin/logout/" + keyTest);
        assertEquals(200, logout.getStatusCode());

        Response testLogin = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createAdminRequest(USER_ONE)))
                .put("/admin/login");
        assertEquals(200, testLogin.getStatusCode());

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
