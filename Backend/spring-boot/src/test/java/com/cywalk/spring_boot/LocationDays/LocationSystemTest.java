package com.cywalk.spring_boot.LocationDays;

import com.cywalk.spring_boot.Locations.*;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class LocationSystemTest {

    @LocalServerPort
    int port;

    private final String BASE_URL = "http://localhost";

    @Autowired
    LocationSessionController locationSessionController;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    PeopleRepository peopleRepository;

    @Autowired
    UserRequestRepository userRequestRepository;

    @Autowired
    UserModelRepository userModelRepository;

    @Autowired
    PeopleService peopleService;

    @Autowired
    LocationService locationService;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    LocationDayRepository locationDayRepository;

    @Autowired
    LocationActivityRepository locationActivityRepository;

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
            throw new RuntimeException(e);
        }
    }

    public static long asIdFromString(String json) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(json).get("id").asLong();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final People testPeople = new People("userOne", "users@email.com", null);
    private final UserRequest basePersonRequest = new UserRequest("userOne", "password123");

    // two locations are curtiss and beardshear
    private final Location Curtiss = new Location(42.026193, -93.645189, 270, null);
    private final Location Beardshear = new Location(42.026181, -93.648049, 270, null);

    // real distance
    private final double realDistance = 150; // very rough google approximation

    private static final String USER_ONE = "userOne";

    @Before
    @Order(1)
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.port = port;
    }

    @Test
    @Order(2)
    @Transactional
    void logLocations() throws Exception {
        if (peopleService.getUserByUsername("userTwo").isPresent()) {
            peopleService.deleteUserByName("userTwo");
        }

        // try to sign up
        Response testSignup = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(asJsonString(createPersonRequest("userTwo")))
                .post("/signup");
        assertEquals(200, testSignup.getStatusCode());
        long key = extractKeyFromResponse(testSignup);

        System.out.println(key);

        RestAssured.given()
                .basePath("/" + key + "/locations/start")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post()
                .then()
                .statusCode(200);

        // now log the key
        RestAssured.given()
                .basePath("/" + key + "/locations/log")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(asJsonString(Curtiss))
                .when()
                .post()
                .then()
                .statusCode(200);

        RestAssured.given()
                .basePath("/" + key + "/locations/log")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(asJsonString(Beardshear))
                .when()
                .post()
                .then()
                .statusCode(200);

        RestAssured.given()
                .basePath("/" + key + "/locations/total")
                .when()
                .get()
                .then()
                .statusCode(200)
                .body(containsString("93"));


        RestAssured.given()
                .basePath("/" + key + "/locations/end")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete()
                .then()
                .statusCode(200);

        /*
        RestAssured.given()
                .basePath("/users/" + key)
                .when()
                .delete()
                .then()
                .statusCode(200);
*/

    }

    private UserRequest createPersonRequest(String username) {
        UserRequest request = new UserRequest();
        request.setUsername(username);
        request.setPassword("password");
        return request;
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
