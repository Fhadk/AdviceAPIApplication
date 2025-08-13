package com.descenedigital.controller;

import com.descenedigital.model.Role;
import com.descenedigital.model.UserAccount;
import com.descenedigital.repo.UserAccountRepo;
import com.descenedigital.security.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.descenedigital.security.JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
@Import(com.descenedigital.config.TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private AuthenticationManager authenticationManager;
    @MockBean private UserAccountRepo userAccountRepo;
    @MockBean private PasswordEncoder passwordEncoder;
    @MockBean private JwtService jwtService;

    @Test
    void register_createsUser() throws Exception {
        Mockito.when(userAccountRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("hash");
        Mockito.when(jwtService.generateToken(anyString(), any())).thenReturn("tkn");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"u\",\"password\":\"p\",\"admin\":false}"))
                .andExpect(status().isOk());
    }

    @Test
    void login_authenticates() throws Exception {
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(new TestingAuthenticationToken("u","p"));
        Mockito.when(jwtService.generateToken(anyString(), any())).thenReturn("tkn");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"u\",\"password\":\"p\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void register_assignsUserRole_whenAdminFalse() throws Exception {
        Mockito.when(userAccountRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("hash");
        Mockito.when(jwtService.generateToken(anyString(), any())).thenReturn("tkn");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"useronly\",\"password\":\"p\",\"admin\":false}"))
                .andExpect(status().isOk());

        ArgumentCaptor<UserAccount> accountCaptor = ArgumentCaptor.forClass(UserAccount.class);
        Mockito.verify(userAccountRepo).save(accountCaptor.capture());
        Set<Role> roles = accountCaptor.getValue().getRoles();
        org.junit.jupiter.api.Assertions.assertEquals(Set.of(Role.USER), roles);

        ArgumentCaptor<Map<String, Object>> claimsCaptor = ArgumentCaptor.forClass(Map.class);
        Mockito.verify(jwtService).generateToken(Mockito.eq("useronly"), claimsCaptor.capture());
        Object rolesClaim = claimsCaptor.getValue().get("roles");
        org.junit.jupiter.api.Assertions.assertTrue(rolesClaim instanceof Set);
        org.junit.jupiter.api.Assertions.assertEquals(Set.of(Role.USER), rolesClaim);
    }

    @Test
    void register_assignsAdminRole_whenAdminTrue() throws Exception {
        Mockito.when(userAccountRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("hash");
        Mockito.when(jwtService.generateToken(anyString(), any())).thenReturn("tkn");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"adminu\",\"password\":\"p\",\"admin\":true}"))
                .andExpect(status().isOk());

        ArgumentCaptor<UserAccount> accountCaptor = ArgumentCaptor.forClass(UserAccount.class);
        Mockito.verify(userAccountRepo).save(accountCaptor.capture());
        Set<Role> roles = accountCaptor.getValue().getRoles();
        org.junit.jupiter.api.Assertions.assertEquals(Set.of(Role.ADMIN, Role.USER), roles);

        ArgumentCaptor<Map<String, Object>> claimsCaptor = ArgumentCaptor.forClass(Map.class);
        Mockito.verify(jwtService).generateToken(Mockito.eq("adminu"), claimsCaptor.capture());
        Object rolesClaim = claimsCaptor.getValue().get("roles");
        org.junit.jupiter.api.Assertions.assertTrue(rolesClaim instanceof Set);
        org.junit.jupiter.api.Assertions.assertEquals(Set.of(Role.ADMIN, Role.USER), rolesClaim);
    }

    @Test
    void register_existingUsername_returnsBadRequest() throws Exception {
        Mockito.when(userAccountRepo.findByUsername(anyString())).thenReturn(Optional.of(new UserAccount()));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"dup\",\"password\":\"p\",\"admin\":false}"))
                .andExpect(status().isBadRequest());

        Mockito.verify(userAccountRepo, Mockito.never()).save(any());
        Mockito.verify(jwtService, Mockito.never()).generateToken(anyString(), any());
    }
}
