package com.descenedigital.integration;

import com.descenedigital.dto.LoginRequest;
import com.descenedigital.model.Advice;
import com.descenedigital.model.Role;
import com.descenedigital.model.User;
import com.descenedigital.repo.AdviceRepo;
import com.descenedigital.repo.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AdviceRepo adviceRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private final String adminEmail = "admin@example.com";
    private final String adminPassword = "adminpass";

    private Long adviceId;

    @BeforeEach
    public void setup() {
        userRepo.deleteAll();
        adviceRepo.deleteAll();

        User adminUser = User.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .build();

        userRepo.save(adminUser);

        Advice advice = Advice.builder()
                .message("Test advice")
                .createdAt(LocalDateTime.now())
                .build();
        Advice savedAdvice = adviceRepo.save(advice);
        adviceId = savedAdvice.getId();
    }

    @Test
    public void whenAccessProtectedEndpointWithoutToken_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/advice/" + adviceId))
                .andExpect(status().isForbidden()); // 403 expected by Spring Security
    }

    @Test
    public void whenLoginAndAccessProtectedEndpointWithToken_thenSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest(adminEmail, adminPassword);

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String jwtToken = loginResponse.replace("\"", "");

        mockMvc.perform(get("/api/advice/" + adviceId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }
}
