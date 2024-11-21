package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleController;
import com.cywalk.spring_boot.Users.PeopleService;
import com.cywalk.spring_boot.Users.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
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
@RunWith(SpringRunner.class)
class FriendControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    PeopleService peopleService;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    public static String asJsonString(Object o) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static long asKeyFromString(String json) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(json).get("key").asLong();
        } catch (Exception e) {
            System.out.println("\n\nFAILED FOR STRING: " + json + "\n");
            throw new RuntimeException(e);
        }
    }

    private final People basePeople = new People("base", "base@email.com", null);
    private final People testPeople = new People("userOne", "users@email.com", null);

    private final UserRequest basePersonRequest = new UserRequest("base", "password123");
    private final UserRequest testPeopleRequest = new UserRequest("userOne", "password123");


    @Transactional
    @Test
    void approveFriendRequest() throws Exception {

        if (peopleService.getUserByUsername("userOne").isPresent()) {
            peopleService.deleteUserByName("userOne");
        }
        if (peopleService.getUserByUsername("base").isPresent()) {
            peopleService.deleteUserByName("base");
        }

        Long keyTest;
        Long keyBase;

        // sign up both users
        Response baseSignup = RestAssured.given().
                header("Content-Type", "text/plain").header("charset", "utf-8").
                body(asJsonString(basePersonRequest))
                .when()
                .post("/signup").thenReturn();

        int statusCode;

        statusCode = baseSignup.getStatusCode();
        assertEquals(statusCode, 200);

        keyBase = asKeyFromString(baseSignup.getBody().asString());

        Response testSignup = RestAssured.given().
                body(asJsonString(testPeopleRequest))
                .when().post("/signup");

        statusCode = testSignup.getStatusCode();
        assertEquals(statusCode, 200);

        keyTest = asKeyFromString(testSignup.getBody().asString());

        Response friendRequestOne = when().post("/friends/" + keyTest + "/request/base");
        statusCode = friendRequestOne.getStatusCode();
        assertEquals(statusCode, 200);

        // check if we have a waiting friend request
        Response listAllFriends = RestAssured.get("/friends/all");
        assertEquals(200, listAllFriends.getStatusCode());

        Response friendRequests = RestAssured.get("/friends/requests/" + keyBase);
        assertEquals(200, friendRequests.getStatusCode());
        assertTrue(friendRequests.getBody().toString().contains("userOne"));

        // check that no one is anyone's friends yet
        Response friendsOfBase = RestAssured.get("/friends/" + keyBase);
        assertEquals(200, friendsOfBase.getStatusCode());
        assertFalse(friendsOfBase.getBody().toString().contains("userOne"));

        Response friendsOfTest = RestAssured.get("/friends/" + keyTest);
        assertEquals(200,friendsOfTest.getStatusCode());
        assertFalse(friendsOfTest.getBody().toString().contains("base"));

        Response friendRequestsBase = RestAssured.get("/friends/requests/" + keyBase);
        assertEquals(200,  friendRequestsBase.getStatusCode());
        assertTrue(friendRequestsBase.getBody().toString().contains("userOne"));

        // accept the request
        assertEquals(200, RestAssured.put("/friends/" + keyBase + "/request/approve/userOne").getStatusCode());

        // ensure there are no outgoing requests for either user
        friendRequestsBase = RestAssured.get("/friends/requests/" + keyBase);
        assertEquals(200,  friendRequestsBase.getStatusCode());
        assertFalse(friendRequestsBase.getBody().toString().contains("userOne"));

        friendRequests = RestAssured.get("/friends/requests/" + keyTest);
        assertEquals(200,  friendRequests.getStatusCode());
        assertFalse(friendRequests.getBody().toString().contains("base"));

        // ensure the friends have each other as friends
        friendsOfBase = RestAssured.get("/friends/" + keyBase);
        assertEquals(200, friendsOfBase.getStatusCode());
        assertTrue(friendsOfBase.getBody().toString().contains("userOne"));

        friendsOfTest = RestAssured.get("/friends/" + keyTest);
        assertEquals(200,friendsOfTest.getStatusCode());
        assertTrue(friendsOfTest.getBody().toString().contains("base"));

        // defer
        assertEquals(200, RestAssured.delete("/users/" + keyBase).getStatusCode());
        assertEquals(200, RestAssured.delete("/users/" + keyTest).getStatusCode());
    }

}
