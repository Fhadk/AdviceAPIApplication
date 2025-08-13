package com.descenedigital.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdviceControllerTests {

    @Autowired
    private MockMvc mockMvc;



    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setup() throws Exception {


        // Register Admin
        Map<String, String> adminReg = new HashMap<>();
        adminReg.put("username", "admin1");
        adminReg.put("password", "pass123");
        adminReg.put("role", "ADMIN");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminReg)))
                .andExpect(status().isOk());

        // Register User
        Map<String, String> userReg = new HashMap<>();
        userReg.put("username", "user1");
        userReg.put("password", "pass123");
        userReg.put("role", "USER");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userReg)))
                .andExpect(status().isOk());

        // Login Admin
        Map<String, String> adminLogin = new HashMap<>();
        adminLogin.put("username", "admin1");
        adminLogin.put("password", "pass123");

        String adminRes = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        adminToken = objectMapper.readTree(adminRes).get("token").asText();

        // Login User
        Map<String, String> userLogin = new HashMap<>();
        userLogin.put("username", "user1");
        userLogin.put("password", "pass123");

        String userRes = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLogin)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        userToken = objectMapper.readTree(userRes).get("token").asText();
    }

    @Test
    void adminCanCreateAdvice() throws Exception {
        Map<String, String> advice = new HashMap<>();
        advice.put("content", "Always write tests.");

        mockMvc.perform(post("/api/advice/admin")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(advice)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Always write tests."));
    }

    @Test
    void userCannotCreateAdvice() throws Exception {
        Map<String, String> advice = new HashMap<>();
        advice.put("content", "This should fail.");

        mockMvc.perform(post("/api/advice/admin")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(advice)))
                .andExpect(status().isForbidden());
    }

    @Test
    void userCanGetAdviceList() throws Exception {
        mockMvc.perform(get("/api/advice")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }
}

