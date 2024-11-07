package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleController;
import com.cywalk.spring_boot.Users.PeopleService;
import com.cywalk.spring_boot.Users.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class FriendControllerTest {

    @Autowired
    private PeopleController controller;

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

        MvcResult checkIfUserOneExists = this.mockMvc.perform(get("/users/username/userOne"))

        if (peopleService.getUserByUsername("userOne").isPresent()) {
            peopleService.deleteUserByName("userOne");
        }
        if (peopleService.getUserByUsername("base").isPresent()) {
            peopleService.deleteUserByName("base");
        }

        Long keyTest;
        Long keyBase;

        // sign up both users
        MvcResult baseSignup = this.mockMvc.perform(post("/signup").
                content(asJsonString(basePersonRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        keyBase = asKeyFromString(baseSignup.getResponse().getContentAsString());

        MvcResult testSignup = this.mockMvc.perform(post("/signup").
                        content(asJsonString(testPeopleRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        keyTest = asKeyFromString(testSignup.getResponse().getContentAsString());

        // now we can actually friend another user
        // test user (userOne) trys to friend base user (base)


        this.mockMvc.perform(
                post("/friends/" + keyTest + "/request/base")
        ).andExpect(status().isOk());

        // check if we have a waiting friend request

        this.mockMvc.perform(
                get("/friends/all")
        ).andExpect(status().isOk());


        this.mockMvc.perform(
                get("/friends/requests/" + keyBase)
        ).andExpect(status().isOk()).andExpect(content().string(containsString("userOne")));


	    this.mockMvc.perform(
	        put("/friends/" + keyBase + "/request/approve/userOne")
    	).andExpect(status().isOk());

        this.mockMvc.perform(
                get("/friends/requests/" + keyBase)
        ).andExpect(status().isOk()).andExpect(content().string(containsString("[]")));

        this.mockMvc.perform(
                get("/friends/" + keyBase)
        ).andExpect(status().isOk()).andExpect(content().string(containsString("userOne")));

        this.mockMvc.perform(
                get("/friends/" + keyTest)
        ).andExpect(status().isOk()).andExpect(content().string(containsString("base")));


        // defer
        this.mockMvc.perform(
                delete("/users/" + keyBase)
        ).andExpect(status().isOk());

        this.mockMvc.perform(
                delete("/users/" + keyTest)
        ).andExpect(status().isOk());

    }

}
