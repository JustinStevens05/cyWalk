package com.cywalk.spring_boot;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class EthanSystemTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    public void testSetStepGoal_Success() throws JSONException {

        JSONObject userRequest = new JSONObject();
        userRequest.put("username", "ethan");
        userRequest.put("password", "password123");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userRequest.toString())
                .when()
                .post("/signup")
                .then()
                .statusCode(200);

        JSONObject stepGoal = new JSONObject();
        stepGoal.put("dailyGoal", 10000);
        stepGoal.put("weeklyGoal", 70000);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(stepGoal.toString())
                .when()
                .post("/goals/ethan");

        assertEquals(200, response.getStatusCode());

        JSONObject responseBody = new JSONObject(response.getBody().asString());
        assertEquals(10000, responseBody.getInt("dailyGoal"));
        assertEquals(70000, responseBody.getInt("weeklyGoal"));
        assertEquals("ethan", responseBody.getJSONObject("people").getString("username"));
    }

    @Test
    public void testDeleteStepGoal_Success() throws JSONException {

        JSONObject userRequest = new JSONObject();
        userRequest.put("username", "ethan");
        userRequest.put("password", "password123");

        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(userRequest.toString())
                .when()
                .post("/signup")
                .then()
                .statusCode(200);

        JSONObject stepGoal = new JSONObject();
        stepGoal.put("dailyGoal", 10000);
        stepGoal.put("weeklyGoal", 70000);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(stepGoal.toString())
                .when()
                .post("/goals/ethan")
                .then()
                .statusCode(200);

        Response deleteResponse = RestAssured.given()
                .when()
                .delete("/goals/ethan");

        assertEquals(200, deleteResponse.getStatusCode());

        RestAssured.given()
                .when()
                .get("/goals/ethan")
                .then()
                .statusCode(404);
    }

    @Test
    public void testSetStepGoal_UserNotFound() throws JSONException {

        JSONObject stepGoal = new JSONObject();
        stepGoal.put("dailyGoal", 10000);
        stepGoal.put("weeklyGoal", 70000);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(stepGoal.toString())
                .when()
                .post("/goals/nonexistentUser");

        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testSetStepGoal_InvalidGoals() throws JSONException {

        JSONObject userRequest = new JSONObject();
        userRequest.put("username", "ethan");
        userRequest.put("password", "password123");

        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(userRequest.toString())
                .when()
                .post("/signup")
                .then()
                .statusCode(200);

        JSONObject stepGoal = new JSONObject();
        stepGoal.put("dailyGoal", -1000);
        stepGoal.put("weeklyGoal", -7000);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(stepGoal.toString())
                .when()
                .post("/goals/ethan");

        assertEquals(404, response.getStatusCode());
    }
}