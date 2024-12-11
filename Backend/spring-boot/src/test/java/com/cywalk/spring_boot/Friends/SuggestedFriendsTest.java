package com.cywalk.spring_boot.Friends;

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
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class SuggestedFriendsTest {
    /*
    @Autowired
    private PeopleService peopleService;
     */

    @LocalServerPort
    int port;

    private final String BASE_URL = "http://localhost";

    private static Long keyUserOne;
    private static Long keyUserTwo;
    private static Long keyUserThree;
    private static Long keyUserFour;
    private static Long keyUserFive;

    private static final String userOne = "userOne";
    private static final String userTwo = "base";
    private static final String userThree = "ckugel";
    private static final String userFour = "jdoe";
    private static final String getUserFive = "cpd";

    /*
    @Mock
    private PeopleRepository peopleRepository;

    @Mock
    private FriendRequestRepository friendRequestRepository;
     */

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserModelRepository userModelRepository;

    @Autowired
    private UserRequestRepository userRequestRepository;

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private FriendService friendService;

    @Before
    @Order(1)
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.port = port;
    }

    @Transactional
    void removeUser(String username) {
        if (peopleService.getUserByUsername(username).isPresent()) {
            peopleService.deleteUserByName(username);
        }
    }

    @Test
    @Transactional
    @Order(1)
    void cleanupExistingUsers() {
        removeUser(userOne);
        removeUser(userTwo);
        removeUser(userThree);
        removeUser(userFour);
        removeUser(getUserFive);
    }

    @Test
    @Order(2)
    @Transactional
    void signUpUsers() {
        setup();
        cleanupExistingUsers();

        assertTrue(peopleService.getUserByUsername(userOne).isEmpty());

        // Sign up test user
        Response testSignup = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createPersonRequest(userOne)))
                .post("/signup");
        assertEquals(200, testSignup.getStatusCode());
        keyUserOne = extractKeyFromResponse(testSignup);

        // sign up user two
        Response userTwoSignup = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createPersonRequest(userTwo)))
                .post("/signup");
        keyUserTwo = extractKeyFromResponse(userTwoSignup);
        assertEquals(200, userTwoSignup.getStatusCode());

        // sign up user three
        Response userThreeSignup = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createPersonRequest(userThree)))
                .post("/signup");
        keyUserThree = extractKeyFromResponse(userThreeSignup);
        assertEquals(200, userThreeSignup.getStatusCode());
        // sign up user four
        Response userFourSignup = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createPersonRequest(userFour)))
                .post("/signup");
        keyUserFour = extractKeyFromResponse(userFourSignup);
        assertEquals(200, userFourSignup.getStatusCode());

        // sign up user five
        Response userFiveSignup = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createPersonRequest(getUserFive)))
                .post("/signup");

        keyUserFive = extractKeyFromResponse(userFiveSignup);
        assertEquals(200, userFiveSignup.getStatusCode());
    }


    @Test
    @Order(3)
    @Transactional
    void handleFriendRequest() {
        cleanupExistingUsers();
        // Send friend request
        Response friendRequest = RestAssured.post("/friends/" + keyUserOne + "/request/" + userTwo);
        assertEquals(200, friendRequest.getStatusCode());

        // Check friend requests for BASE_USER
        Response friendRequests = RestAssured.get("/friends/requests/" + keyUserTwo);
        assertEquals(200, friendRequests.getStatusCode());
        assertTrue(friendRequests.getBody().asString().contains(userOne));

        // Approve the friend request
        Response approveRequest = RestAssured.put("/friends/" + keyUserTwo + "/request/approve/" + userOne);
        assertEquals(200, approveRequest.getStatusCode());

        // Verify both users are friends
        Response friendsOfBase = RestAssured.get("/friends/" + keyUserTwo);
        assertEquals(200, friendsOfBase.getStatusCode());
        assertTrue(friendsOfBase.getBody().asString().contains(userOne));

        Response friendsOfTest = RestAssured.get("/friends/" + keyUserOne);
        assertEquals(200, friendsOfTest.getStatusCode());
        assertTrue(friendsOfTest.getBody().asString().contains(userTwo));

        // now we cna skip a lot of checks and assertions by trusting that the above code works
        // user 2 request and approve user 3
        Response friendRequest2 = RestAssured.post("/friends/" + keyUserTwo + "/request/" + userThree);
        assertEquals(200, friendRequest2.getStatusCode());

        Response approveRequest2 = RestAssured.put("/friends/" + keyUserThree + "/request/approve/" + userTwo);
        assertEquals(200, approveRequest2.getStatusCode());

        // user 3 request and approve user 4
        Response friendRequest3 = RestAssured.post("/friends/" + keyUserThree + "/request/" + userFour);
        assertEquals(200, friendRequest3.getStatusCode());

        Response approveRequest3 = RestAssured.put("/friends/" + keyUserFour + "/request/approve/" + userThree);
        assertEquals(200, approveRequest3.getStatusCode());

        // 4 request and approve 2
        Response friendRequest4 = RestAssured.post("/friends/" + keyUserFour + "/request/" + userTwo);
        assertEquals(200, friendRequest4.getStatusCode());

        Response approveRequest4 = RestAssured.put("/friends/" + keyUserTwo + "/request/approve/" + userFour);
        assertEquals(200, approveRequest4.getStatusCode());

        // 1 request and approve 5
        Response friendRequest5 = RestAssured.post("/friends/" + keyUserOne + "/request/" + getUserFive);
        assertEquals(200, friendRequest5.getStatusCode());

        Response approveRequest5 = RestAssured.put("/friends/" + keyUserFive + "/request/approve/" + userOne);
        assertEquals(200, approveRequest5.getStatusCode());

        // now we get suggested friends for user one
        // we should expect to see users 3 and 4 and expect to NOT see user 5
        Response suggestedFriends = RestAssured.put("/friends/" + keyUserOne + "/suggested");
        assertEquals(200, suggestedFriends.getStatusCode());

        assertTrue(suggestedFriends.getBody().asString().contains(userThree));
        assertTrue(suggestedFriends.getBody().asString().contains(userFour));
        assertFalse(suggestedFriends.getBody().asString().contains(getUserFive));
    }


    @Test
    @Order(4)
    @Transactional
    void cleanup() {
        // Delete both users
        /*
        Response deleteBase = RestAssured.delete("/users/" + keyBase);
        assertEquals(200, deleteBase.getStatusCode());

        Response deleteTest = RestAssured.delete("/users/" + keyTest);
        assertEquals(200, deleteTest.getStatusCode());
        */
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
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody().asString());
            return root.path("id").asLong();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
