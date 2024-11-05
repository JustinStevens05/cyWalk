package com.cywalk.spring_boot.LocationDays;

import ch.qos.logback.core.joran.util.ParentTag_Tag_Class_Tuple;
import com.cywalk.spring_boot.Locations.Location;
import com.cywalk.spring_boot.Locations.LocationController;
import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleController;
import com.cywalk.spring_boot.Users.PeopleService;
import com.cywalk.spring_boot.Users.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.junit.jupiter.api.*;
import java.io.UnsupportedEncodingException;
import java.time.LocalTime;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class LocationDayControllerTest {


    @Autowired
    private LocationController locationController;

    @Autowired
    private LocationDayController locationDayController;

    @Autowired
    PeopleService peopleService;

    @Autowired
    private MockMvc mockMvc;

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

    @Transactional
    @Test
    void logLocations() throws Exception {
        if (peopleService.getUserByUsername("userOne").isPresent()) {
            peopleService.deleteUserByName("userOne");
        }

        // try to sign up
        MvcResult result = this.mockMvc.perform(
                post("/signup")
                .content(asJsonString(basePersonRequest))
                .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String resultAsString = result.getResponse().getContentAsString();
        long key = asKeyFromString(resultAsString);
        System.out.println(key);

        // now log the key
        this.mockMvc.perform(
                post("/" + key + "/locations/log").content(asJsonString(Curtiss)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        this.mockMvc.perform(
                post("/" + key + "/locations/log").content(asJsonString(Beardshear)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        this.mockMvc.perform(
                get("/" + key + "/location/total")
        ).andExpect(status().isOk());

        this.mockMvc.perform(
                delete("/users/" + key)
        ).andExpect(status().isOk());



       //  System.out.println(result1.getResponse().getContentAsString());
    }

    @Test
    void contextLoads() {
        assertThat(locationController).isNotNull();
        assertThat(locationDayController).isNotNull();
        assertThat(peopleService).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

}