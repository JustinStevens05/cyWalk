package com.cywalk.spring_boot.Admins;

import com.cywalk.spring_boot.Friends.FriendRequestRepository;
import com.cywalk.spring_boot.Friends.FriendService;
import com.cywalk.spring_boot.Organizations.OrganizationRepository;
import com.cywalk.spring_boot.Users.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.annotation.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SignUpLoginTest {
    /*
    @Autowired
    private PeopleService peopleService;
     */

    @LocalServerPort
    int port;

    private final String BASE_URL = "http://localhost";

    private static Long keyBase;

    private static final String USER_ONE = "userOne";
    private static final String user =  "cpd";

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

    @Before
    @Order(1)
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.port = port;
    }


    @Test
    @Order(2)
    void SignUpAdmin() {
        setup();


        // Sign up test user
        Response testSignup = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createAdminRequest(USER_ONE)))
                .post("/signup");
        assertEquals(200, testSignup.getStatusCode());
        double keyTest = extractKeyFromResponse(testSignup);

        // sign up model user to organization
        Response testSignup2 = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createAdminRequest(user)))
                .post("/signup");
        assertEquals(200, testSignup2.getStatusCode());
        double userKey = extractKeyFromResponse(testSignup2);


    }

    private AdminModel createAdminRequest(String username) {
        AdminModel request = new AdminModel();
        request.setUsername(username);
        request.setPassword("password");
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
}
