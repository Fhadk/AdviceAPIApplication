package com.descenedigital.e2e;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.descenedigital.AdviceApiApplication;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AdviceApiApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class AdviceFlowE2ETest {

    private static String adminToken;
    private static String userToken;
    private static Long adviceId;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void setup(@Autowired MockMvc mockMvc) throws Exception {
        // register admin
        String adminRegister = "{\"username\":\"admin1\",\"password\":\"adminPass1!\",\"admin\":true}";
        adminToken = extractToken(performPost(mockMvc, "/api/auth/register", adminRegister));

        // register user
        String userRegister = "{\"username\":\"user1\",\"password\":\"userPass1!\",\"admin\":false}";
        userToken = extractToken(performPost(mockMvc, "/api/auth/register", userRegister));
    }

    @Test
    @Order(1)
    @DisplayName("ADMIN can create advice")
    public void adminCanCreate(@Autowired MockMvc mockMvc) throws Exception {
        String body = "{\"message\":\"always test your code\"}";
        MvcResult res = mockMvc.perform(post("/api/advice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.message").value("always test your code"))
                .andReturn();
        JsonNode node = mapper.readTree(res.getResponse().getContentAsString());
        adviceId = node.get("id").asLong();
    }

    @Test
    @Order(2)
    @DisplayName("USER cannot create advice (forbidden)")
    public void userCannotCreate(@Autowired MockMvc mockMvc) throws Exception {
        String body = "{\"message\":\"user not allowed\"}";
        mockMvc.perform(post("/api/advice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + userToken)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(3)
    @DisplayName("List advice with pagination")
    public void listAdvice(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get("/api/advice?page=0&size=10&sort=id,desc")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @Order(4)
    @DisplayName("Get advice by id")
    public void getAdvice(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get("/api/advice/" + adviceId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(adviceId));
    }

    @Test
    @Order(5)
    @DisplayName("ADMIN can update advice")
    public void adminCanUpdate(@Autowired MockMvc mockMvc) throws Exception {
        String body = "{\"message\":\"write tests first\"}";
        mockMvc.perform(put("/api/advice/" + adviceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("write tests first"));
    }

    @Test
    @Order(6)
    @DisplayName("USER cannot delete advice (forbidden)")
    public void userCannotDelete(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(delete("/api/advice/" + adviceId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(7)
    @DisplayName("ADMIN can delete advice")
    public void adminCanDelete(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(delete("/api/advice/" + adviceId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

    private static String extractToken(MvcResult result) throws Exception {
        JsonNode node = mapper.readTree(result.getResponse().getContentAsString());
        return node.get("token").asText();
    }

    private static MvcResult performPost(MockMvc mockMvc, String url, String body) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();
    }
}
