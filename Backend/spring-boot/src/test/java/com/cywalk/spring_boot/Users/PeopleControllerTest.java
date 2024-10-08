package com.cywalk.spring_boot.Users;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.DisplayName.class)
class PeopleControllerTest {

    @Autowired
    private PeopleController controller;

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

    private final People basePeople = new People("base", "base@email.com", null);
    private final People testPeople = new People("userOne", "users@email.com", null);

    /*
    void init() throws Exception {
        this.mockMvc.perform(post("/users").content(asJsonString(testPeople))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }
    */

    @Transactional
    @Test
    void AAClearBase() throws Exception {
        this.mockMvc.perform(delete("/users/username/base"))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    void ACreateUser() throws Exception {
        this.mockMvc.perform(post("/users").content(asJsonString(basePeople))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("base@email")));
    }

    @Transactional
    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
        assertThat(mockMvc).isNotNull();
    }
/*
    @Transactional
    @Test
    void CGetUserByUsername() throws Exception {
        this.mockMvc.perform(get("/users/username/base")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("base@email")));
    }
*/
    @Transactional
    @Test
    void DeleteUser() throws Exception {
        this.mockMvc.perform(delete("/users/username/base"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/users/username/base"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("null")));
    }


}