package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.AssertTrue;
import org.h2.engine.User;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class FriendControllerTest {
    /*
    @Autowired
    private PeopleService peopleService;
     */

    @LocalServerPort
    int port;

    private final String BASE_URL = "http://localhost";

    private static Long keyBase;
    private static Long keyTest;

    private static final String USER_ONE = "userOne";
    private static final String BASE_USER = "base";

    /*
    @Mock
    private PeopleRepository peopleRepository;

    @Mock
    private FriendRequestRepository friendRequestRepository;
     */

    @MockBean
    private FriendService friendService;

    @MockBean
    private PeopleService peopleService;

    @Autowired
    private PeopleController peopleController;

    @Autowired
    private FriendController friendController;
    /*

    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.port = port;

    }

    @Test
    @Order(1)
    void cleanupExistingUsers() {
        if (peopleService.getUserByUsername(USER_ONE).isPresent()) {
            peopleService.deleteUserByName(USER_ONE);
        }
        if (peopleService.getUserByUsername(BASE_USER).isPresent()) {
            peopleService.deleteUserByName(BASE_USER);
        }
        assertTrue(peopleService.getUserByUsername(USER_ONE).isEmpty());
    }

    @Test
    @Order(2)
    void signupUsers() {
        // Sign up base user
        Response baseSignup = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createPersonRequest(BASE_USER)))
                .post("/signup");
        assertEquals(200, baseSignup.getStatusCode());
        keyBase = extractKeyFromResponse(baseSignup);

        // Sign up test user
        Response testSignup = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createPersonRequest(USER_ONE)))
                .post("/signup");
        assertEquals(200, testSignup.getStatusCode());
        keyTest = extractKeyFromResponse(testSignup);
    }

    @Test
    @Order(3)
    void handleFriendRequest() {
        // Send friend request
        Response friendRequest = RestAssured.given().header("Content-Type", "application/json").body("").post("/friends/" + keyTest + "/request/" + BASE_USER);
        assertEquals(200, friendRequest.getStatusCode());

        // Check friend requests for BASE_USER
        Response friendRequests = RestAssured.get("/friends/requests/" + keyBase);
        assertEquals(200, friendRequests.getStatusCode());
        assertTrue(friendRequests.getBody().asString().contains(USER_ONE));

        // Approve the friend request
        Response approveRequest = RestAssured.put("/friends/" + keyBase + "/request/approve/" + USER_ONE);
        assertEquals(200, approveRequest.getStatusCode());

        // Verify both users are friends
        Response friendsOfBase = RestAssured.get("/friends/" + keyBase);
        assertEquals(200, friendsOfBase.getStatusCode());
        assertTrue(friendsOfBase.getBody().asString().contains(USER_ONE));

        Response friendsOfTest = RestAssured.get("/friends/" + keyTest);
        assertEquals(200, friendsOfTest.getStatusCode());
        assertTrue(friendsOfTest.getBody().asString().contains(BASE_USER));
    }

    @Test
    @Order(4)
    void cleanup() {
        // Delete both users
        Response deleteBase = RestAssured.delete("/users/" + keyBase);
        assertEquals(200, deleteBase.getStatusCode());

        Response deleteTest = RestAssured.delete("/users/" + keyTest);
        assertEquals(200, deleteTest.getStatusCode());
    }

    private UserRequest createPersonRequest(String username) {
        UserRequest request = new UserRequest();
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
        // Assumes response body contains the key
        return Long.parseLong(response.getBody().asString());
    }
     */

}
