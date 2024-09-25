package com.cywalk.spring_boot.Users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.DisplayName.class)
class UserControllerTest {

    @Autowired
    private UserController controller;

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

    private final User baseUser = new User(1, "base", "base@email.com", null);
    private final User testUser = new User(2, "userOne", "users@email.com", null);

    @BeforeTestExecution
    void init() throws Exception {
        this.mockMvc.perform(post("/users").content(asJsonString(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    void ACreateUser() throws Exception {
        this.mockMvc.perform(post("/users").content(asJsonString(baseUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("base@email")));
    }

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void CGetUserById() throws Exception {
        this.mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("base")));
    }

    @Test
    void CGetUserByUsername() throws Exception {
        this.mockMvc.perform(get("/users/username/base")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("base@email")));
    }



    @Test
    void DeleteUser() throws Exception {
        this.mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("null")));
    }
}